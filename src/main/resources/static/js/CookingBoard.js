/**
 * The DOM element[s] pertaining to the cooking board with all present
 * utensils and ingredients.
 */
class CookingBoard{
    constructor(board_ul, recipe, game_area=null){
        /** the board element*/
        this.board_ul = board_ul;

        /** the recipe steps, if needed*/
        this.recipe = recipe;

        /** place for minigames*/
        this.game_area = game_area;

        /** additional info*/
        this.identifier = 0;
        this.items = {};    //let this be a list of hashes with {DOM id : {item name, imageFileUrl, use, tags, quantity, selected}}
        this.tools = [];
        this.ingredients = [];
        // this.processedItems = [];
        this.actions = [];
        // this.actionPair = {};
        this.saved = true;

        this.relevent_id = null;
        this.relevent_action = null;
    }

    /** MENU ADD ITEM---------------------------------*/
    /**
     * item param should have name
     *      additional optional info: quantity, imageFileUrl
     */
    addItem(item, use, tag=[]){
        // console.log(use);
        let record = {};
        record["name"] = item.name;
        if(tag.length > 0) record["tags"] = [tag];
        else record["tags"] = [];
        record["use"] = use;
        record["quantity"] = item.quantity ? item.quantity : 1;
        record["selected"] = item.selected ? true : false;

        const wrapper = document.createElement("div");
        if(Array.isArray(this.board_ul)){ wrapper.setAttribute("class", "col s4"); }
        else { wrapper.setAttribute("class", "col s2"); }

        const card = document.createElement("div");
        card.setAttribute("class", "card waves-effect waves-teal active-item");
        card.setAttribute("id", this.identifier);

        const card_img = document.createElement("div");
        card_img.setAttribute("class", "card-image");

        const img = document.createElement("img");
        if(item.imageFileUrl != null){
            img.setAttribute("src", item.imageFileUrl)
            record["img"] = item.imageFileUrl;
        } else {
            img.setAttribute("src", "/images/placeholder.png");    //make a placeholder.png later
            record["img"] = "/images/placeholder.png";
        }

        const card_content = document.createElement("div");
        card_content.setAttribute("class", "card-content center");

        const content = document.createElement("p");
        if(use === "ingredient"){
            content.textContent = record.quantity + " " + item.name;
        } else {
            content.textContent = item.name;
        }

        const card_menu = document.createElement("div");
        card_menu.setAttribute("class", "card-menu");

        const card_menu_ul = document.createElement("ul");
        card_menu_ul.setAttribute("class", "card-menu-ul");

        if(use === "ingredient"){
            card.classList.add("ingredient");
            this.addIngredient(item);
        }
        else if(use === "tool"){
            this.addTool(item);
        }
        else if(use === "processedItem"){
            card.classList.add("processedItem");
            content.textContent += ": " + tag;
        } else { console.log("item use not defined"); }

        const card_select = document.createElement("div");
        card_select.setAttribute("class", "card-select");
        if(item.selected) card.classList.add("selected");
        card_select.textContent = "Select";
        card_select.onclick = (e) => {
            e.target.parentNode.classList.toggle("selected");
            this.items[e.target.parentNode.id].selected = !this.items[e.target.parentNode.id].selected;
            this.groupItems();
        };

        card_menu.appendChild(card_menu_ul);
        card_content.appendChild(content);
        card_img.appendChild(img);
        card.appendChild(card_img);
        card.appendChild(card_content);
        card.appendChild(card_menu);
        card.appendChild(card_select);
        wrapper.appendChild(card);

        if(Array.isArray(this.board_ul)){
            if(use === "ingredient") this.board_ul[0].appendChild(wrapper);
            else if(use === "tool") this.board_ul[1].appendChild(wrapper);
            else if(use === "processedItem") this.board_ul[2].appendChild(wrapper);
            else{ console.log("invalid case"); }
        } else {
            this.board_ul.appendChild(wrapper);
        }

        this.items[this.identifier] = record;
        this.identifier++;

        console.log(this.items);
        console.log(this.tools);
        console.log(this.actions);
    }

    addTag(itemKey, tag) {
        if (this.items[itemKey].tags.includes(tag)) return;

        document.getElementById(itemKey).childNodes[1].firstChild.textContent += ", " + tag;

        this.items[itemKey].tags.push(tag);
    }

    /**add to requested ingredient list*/
    addIngredient(item){
        // if(!this.ingredients.includes(item.name)) this.ingredients.push(item);
        for(const ingredient of this.ingredients){
            if(ingredient.name === item.name) return;
        }
        this.ingredients.push(item);
    }

    /**add to requested tools list as known by action menu */
    addTool(item){
        // if(!this.tools.includes(name)) this.tools.push(name);
        let newTool = true;
        for(const tool of this.tools){
            if(tool.name === item.name) return;
        }
        this.tools.push(item);

        for(let action of item.actions){
            if(!this.actions.includes(action))
                this.actions.push(action)
        }
    }

    async updateMenu(){
        let menu_ul;
        for(const id in this.items){
            if(this.items[id].use === "tool") continue;
            const card = document.getElementById(id);
            // console.log(card);
            menu_ul = card.childNodes[2].firstChild;
            while(menu_ul.firstChild){
                menu_ul.removeChild(menu_ul.firstChild);
            }

            let actionList = [];
            for(const tool of this.tools){
                let data = {"ingredient": [this.items[id].name], "tool": [tool.name], "intermediateIngredient": []};
                const newActions = await fetch("/valid-actions", {
                    method: "POST",
                    body: JSON.stringify(data),
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    }
                })
                    .then((response) => response.json())
                    .catch(e => {console.log("err ", e)});

                console.log(newActions);

                for(let action of newActions){
                    if(actionList.includes(action)) continue;

                    const li = document.createElement("li");
                    li.setAttribute("class", "menu-option");
                    li.setAttribute("oncontextmenu", "return false");
                    li.textContent = action;

                    li.onclick = (e) => {
                        if(!this.game_area) {
                            let target = e.target.parentNode.parentNode.previousSibling.firstChild.textContent;

                            if(this.recipe){ this.recipe.addToRecipe(action, target); }

                            this.performAction(id, action);
                            this.saved = false;
                        } else {
                            this.relevent_id = id;
                            this.relevent_action = e.target.textContent;

                            let item = e.target.parentNode.parentNode.parentNode;
                            item.classList.add("game-in-progress");

                            const action = e.target.textContent;
                            // const action = "drizzle";
                            const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
                            tab_instance.select("gameView");
                            this.playGame(action);
                        }
                    };

                    actionList.push(action);
                    menu_ul.appendChild(li);
                }
            }

            if(!menu_ul.firstChild){
                const li = document.createElement("li");
                li.setAttribute("class", "menu-option noSelect");
                li.setAttribute("oncontextmenu", "return false");
                li.textContent = "No actions available";
                menu_ul.appendChild(li);
            }
        }
    }

    performAction(id=this.relevent_id, action=this.relevent_action){
        if (this.items[id].use === "processedItem") {
            this.addTag(id, action, []);
        } else {
            let newItem = {"name": this.items[id].name, "imageFileUrl": this.items[id].img};
            this.addItem(newItem, "processedItem", action);
            this.updateMenu();
        }

        if(this.game_area){
            user_recipe.confirmStep(action, this.items[id].name, this.items[id].quantity);
        }

        this.relevent_id = null;
        this.relevent_action = null;
    }

    failedAction(){
        this.relevent_id = null;
        this.relevent_action = null;
    }

    async playGame(action){
        const path = "/game-app/" + action;
        const game = await fetch(path, {
            method: "GET",
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        console.log(game);
        let head = document.getElementsByTagName("head")[0];
        let gameScript = document.createElement("script");
        gameScript.type = "text/javascript";
        gameScript.src = "/" + game.jsFilePath;
        let gameLink = document.createElement("link");
        gameLink.rel = "stylesheet";
        gameLink.type = "text/css";
        gameLink.href = "/" + game.presentationFilePath;
        head.appendChild(gameLink);
        head.appendChild(gameScript);

        current_game_script = gameScript;
        current_game_css = gameLink;
    }

    // useTool(action){
    //     for(const tool in this.actionPair){
    //         if(this.actionPair[tool].indexOf(action) > -1){
    //             for(const id in this.items){
    //                 if(this.items[id].name === tool){
    //                     let card = document.getElementById(id);
    //                     card.parentNode.parentNode.removeChild(card.parentNode);
    //                     delete this.items[id];
    //                     delete this.actionPair[tool];
    //                     this.tools.filter(item => item.name !== tool)
    //
    //                     this.updateMenu();
    //                     return;
    //                 }
    //             }
    //         }
    //     }
    // }

    groupItems(){
        let groups = {};
        for(const id in this.items) {
            if(this.items[id].selected === true && this.items[id].use === "ingredient"){
                if(groups[this.items[id].name]){
                    groups[this.items[id].name].push(id);
                } else {
                    groups[this.items[id].name] = [id];
                }
            }
        }

        for(const [name, ids] of Object.entries(groups)){
            if(ids.length === 1) continue;

            let newQuant = 0;
            let itemImg = this.items[ids[0]].img;
            for(const id of ids){
                newQuant += this.items[id].quantity;
                const card = document.getElementById(id);
                card.parentNode.parentNode.removeChild(card.parentNode);
                delete this.items[id];
            }
            let newItem = {"name":name, "imageFileUrl":itemImg, "quantity":newQuant, "selected":true};
            this.addItem(newItem, "ingredient", []);
            this.updateMenu();
        }
    }

    resetSelectedItems(){
        for(const id in this.items) {
            if(this.items[id].selected === true){
                const card = document.getElementById(id);
                id.classList.remove("selected");
            }
        }
    }

    getTools(){
        return this.tools;
    }

    getIngredients(){
        return this.ingredients;
    }

    getProcessedItems(){
        const keys = Object.keys(this.items);
        let processedItems = [];
        for(const id of keys){
            if(this.items[id].use === "processedItem"){
                let nameAndTag = "";
                for(const tag of this.items[id].tags)
                    nameAndTag += tag + " ";
                nameAndTag += this.items[id].name;
                processedItems.push(nameAndTag);
            }
        }
        return processedItems;
    }

    setSavedStatus(state){
        this.saved = state;
    }

    getSavedStatus(){
        return this.saved;
    }
}