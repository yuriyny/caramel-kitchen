/**
 * The DOM element[s] pertaining to the cooking board with all present
 * utensils and ingredients.
 */
class CookingBoard{
    constructor(board_ul, recipe, game_area=null, descriptor=null){
        /** the board element*/
        this.board_ul = board_ul;

        /** the recipe steps, if needed*/
        this.recipe = recipe;

        /** place for minigames*/
        this.game_area = game_area;

        /** shown text output*/
        this.descriptor = descriptor;

        /** additional info*/
        this.new_identifier = 1;
        this.items = {};
        this.selectedIds = [];
        this.selectedIngredients = [];
        this.selectedTools = [];
        this.selectedIntermediateIngredients = [];

        this.saved = true;

        this.relevent_id = null;
        this.relevent_action = null;

        this.intermediate_steps = [];

        this.available_games = ["boil", "chop", "flatten", "flip", "marinate", "mix", "peel", "sauté", "slice", "spread", "stir"];
    }

    /** MENU ADD ITEM---------------------------------*/
    /**
     * item param should have name
     *      additional optional info: quantity, imageFileUrl
     */
    addItem(item, use, tag=[]){
        if(use !== "processedItem" && this.items.hasOwnProperty(item.id)){
            M.toast({html:"You already have that item!", displayLength:1000});
            return;
        }
        if(document.getElementsByClassName("preparingItem").length > 0){
            M.toast({html:"Define the current prepared item first!", displayLength:1000});
            return;
        }

        let record = {};
        Object.assign(record, item);
        if(tag.length > 0) record["tags"] = tag;
        else record["tags"] = [];
        record["use"] = use;
        if(item.quantity) record["quantity"] = item.quantity;
        else record["quantity"] = 1;
        if(item.selected) record["selected"] = item.selected;
        else record["selected"] = false;

        const wrapper = document.createElement("div");
        if(Array.isArray(this.board_ul)){ wrapper.setAttribute("class", "col s4"); }
        else { wrapper.setAttribute("class", "col s2"); }

        const card = document.createElement("div");
        card.setAttribute("class", "card waves-effect waves-teal active-item");
        card.setAttribute("id", record.id);

        const card_img = document.createElement("div");
        card_img.setAttribute("class", "card-image");

        const img = document.createElement("img");
        if(item.imageFileUrl != null){
            img.setAttribute("src", item.imageFileUrl);
        } else {
            img.setAttribute("src", "/images/placeholder.png");
            record["imageFileUrl"] = "/images/placeholder.png";
        }

        const card_content = document.createElement("div");
        card_content.setAttribute("class", "card-content center");

        const content = document.createElement("p");
        content.textContent = item.name;

        const card_menu = document.createElement("div");
        card_menu.setAttribute("class", "card-menu");

        const card_menu_ul = document.createElement("ul");
        card_menu_ul.setAttribute("class", "card-menu-ul");

        if(use === "ingredient"){
            card.classList.add("ingredient");
        }
        else if(use === "tool"){}
        else if(use === "processedItem"){
            card.classList.add("processedItem");
        } else { console.log("item use not defined"); }

        const card_select = document.createElement("div");
        card_select.setAttribute("class", "card-select");
        if(item.selected) card.classList.add("selected");
        card_select.textContent = "Select";
        card_select.onclick = (e) => {
            e.target.parentNode.classList.toggle("selected");
            this.items[e.target.parentNode.id].selected = !this.items[e.target.parentNode.id].selected;
            this.setDescriptor();
            this.setSelected();
            this.updateMenu();
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

        this.items[record.id] = record;

        console.log(this.items);
        // console.log(this.getIntermediateSteps());
    }

    async prepareItem(item, tag=[]){
        let record = {};
        Object.assign(record, item);
        if(tag.length > 0) record["tags"] = tag;
        else record["tags"] = [];
        record["id"] = this.new_identifier;
        this.new_identifier++;
        record["use"] = "processedItem";
        record["quantity"] = 1;
        record["selected"] = false;
        record["imageFileUrl"] = null;
        record["imageName"] = null;

        const wrapper = document.createElement("div");
        if(Array.isArray(this.board_ul)){ wrapper.setAttribute("class", "col s4"); }
        else { wrapper.setAttribute("class", "col s2"); }

        const card = document.createElement("div");
        card.setAttribute("class", "card waves-effect waves-teal active-item preparingItem");
        card.setAttribute("id", record.id);

        const card_img = document.createElement("div");
        card_img.setAttribute("class", "card-image");
        card_img.setAttribute("style", "padding: 10px;");

        const notify_text = document.createElement("span");
        notify_text.setAttribute("class", "card-image-text");
        notify_text.textContent = "Click here to upload an image!";
        const img = document.createElement("input");
        img.setAttribute("type", "image");
        img.setAttribute("id", "prepared-card-image")
        img.setAttribute("src", "/images/placeholder.png");
        img.setAttribute("style", "width: 100%; position: absolute; top: 0; left: 0; opacity: 0.5");
        const img_input = document.createElement("input");
        img_input.setAttribute("type", "file");
        img_input.setAttribute("id", "image-input");
        img_input.setAttribute("style", "display: none;");
        img_input.setAttribute("accept", "image/*");
        img.addEventListener("click", ()=>{
            $("#image-input").click();
        });
        img_input.onchange = () =>{
            let preview = img;
            let file = img_input.files[0];
            let reader = new FileReader();
            reader.onloadend = function(){
                preview.src = reader.result;
                record["imageFileUrl"] = reader.result;
                if(document.getElementsByClassName("card-image-text")){
                    document.getElementsByClassName("card-image-text")[0].parentNode.removeChild(document.getElementsByClassName("card-image-text")[0]);
                    document.getElementById("prepared-card-image").style.opacity = "1";
                }
            };
            if(file) {reader.readAsDataURL(file);}
            else { preview.src = "/images/placeholder.png;"}
        };

        const card_content = document.createElement("div");
        card_content.setAttribute("class", "card-content center");

        const card_name_input = document.createElement("input");
        card_name_input.setAttribute("class", "processedItem-name-input browser-default");
        card_name_input.setAttribute("type", "text");
        card_name_input.setAttribute("placeholder", "Item name here");
        card_name_input.addEventListener("keypress", (e)=>{
            if(e.key === "Enter"){
                if(record["imageFileUrl"] !== null && img_input.files[0]) this.uploadImage(img_input.files[0]);

                record["name"] = e.target.value;
                e.target.parentNode.parentNode.parentNode.parentNode.removeChild(e.target.parentNode.parentNode.parentNode);
                this.addIntermediateStep(record);
                this.addItem(record, "processedItem");
                this.updateMenu();
            }
        });
        card_content.appendChild(card_name_input);

        card_img.appendChild(img);
        card_img.appendChild(img_input);
        card_img.appendChild(notify_text);
        card.appendChild(card_img);
        card.appendChild(card_content);
        wrapper.appendChild(card);

        if(Array.isArray(this.board_ul)){
            this.board_ul[2].appendChild(wrapper);
        } else {
            this.board_ul.appendChild(wrapper);
        }
    }

    async uploadImage(file){
        let data = new FormData();
        data.append('file', file);
        const status = await fetch("/upload-image", {
            method: "POST",
            body: data
        })
            .then((response) => response.json())
            .then((response) => response.message)
            .catch(e => {console.log("err ", e)});

        let current_intermediate_step = this.intermediate_steps[this.intermediate_steps.length - 1];
        current_intermediate_step["imageName"] = status;
    }

    addTag(itemKey, tag) {
        if (this.items[itemKey].tags.includes(tag)) return;

        document.getElementById(itemKey).childNodes[1].firstChild.textContent += ", " + tag;

        this.items[itemKey].tags.push(tag);
    }

    async updateMenu(){
        let menu_ul;
        for(const id in this.items){
            if(this.items[id].use === "tool") continue;
            const card = document.getElementById(id);
            menu_ul = card.childNodes[2].firstChild;
            while(menu_ul.firstChild){
                menu_ul.removeChild(menu_ul.firstChild);
            }

            if(this.selectedIds.length > 0){
                if(!this.selectedIds.includes(id)) continue;

                let data = {"ingredient": this.selectedIngredients, "tool": this.selectedTools, "intermediateIngredient": this.selectedIntermediateIngredients};
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

                for(const action of newActions){
                    this.addAction(id, action);
                }
            }
            else if(this.items[id].use === "ingredient"){
                let actionList = [];
                let toolList = this.getTools();
                for (const tool of toolList) {
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
                        .catch(e => {
                            console.log("err ", e)
                        });

                    for (let action of newActions) {
                        if (actionList.includes(action)) continue;

                        this.addAction(id, action);
                        actionList.push(action);
                    }
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

    addAction(id, action){
        const card = document.getElementById(id);
        let menu_ul = card.childNodes[2].firstChild;

        const li = document.createElement("li");
        li.setAttribute("class", "menu-option");
        li.setAttribute("oncontextmenu", "return false");
        li.textContent = action;

        li.onclick = (e) => {
            if(document.getElementsByClassName("preparingItem").length > 0){
                M.toast({html:"Define the current prepared item first!", displayLength:1000});
                return;
            }

            if(!this.game_area) {
                let targets = [];
                if(this.selectedIds.length > 0){
                    let ingredients = [];
                    let intermediate_ingredients = [];
                    for(const id of this.selectedIds){
                        if(this.items[id].use !== "tool") targets.push(this.items[id]);
                        if(this.items[id].use === "ingredient") ingredients.push(this.items[id]);
                        if(this.items[id].use === "processedItem") intermediate_ingredients.push(this.items[id]);
                    }
                    if(this.recipe){
                        this.recipe.addToRecipe(action, targets);
                    }
                    this.performAction(this.selectedIngredients, e.target.textContent);
                } else {
                    let targetId = e.target.parentNode.parentNode.parentNode.id;
                    let ingredient = this.items[targetId];
                    if(this.recipe){
                        this.recipe.addToRecipe(action, [ingredient]);
                    }
                    this.prepareItem(ingredient);
                }

                this.updateMenu();
                this.saved = false;
            } else {
                if(this.selectedIds.length > 0){
                    this.relevent_id = [];
                    for(let id of this.selectedIds){
                        if(this.items[id].use !== "tool") {
                            this.relevent_id.push(id);
                        }
                    }
                }
                else{ this.relevent_id = id; }
                this.relevent_action = e.target.textContent;

                if(this.selectedIds.length > 0) {
                    for (const id of this.selectedIds) {
                        if (this.items[id].use !== "tool"){
                            let card = document.getElementById(id);
                            card.classList.add("game-in-progress");
                        }
                    }
                } else {
                    let item = e.target.parentNode.parentNode.parentNode;
                    item.classList.add("game-in-progress");
                }

                const action = e.target.textContent;
                // const action = "drizzle";
                const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
                tab_instance.select("gameView");
                this.playGame(action);
            }
        };

        menu_ul.appendChild(li);
    }

    performAction(id=this.relevent_id, action=this.relevent_action){
        if(Array.isArray(id)){
            if (this.game_area) {
                let targets = [];
                for(let identifier of id){
                    targets.push(this.items[identifier]);
                }
                this.recipe.confirmStep(action, targets);
            }
            else {
                let newItem = {};
                newItem.name = "mixed item placeholder";
                newItem.id = this.new_identifier;
                this.new_identifier++;
                this.prepareItem(newItem, [action]);
            }
        }

        else {
            if (this.game_area) {
                this.recipe.confirmStep(action, [this.items[id]]);
            }
            // else if (this.items[id].use === "processedItem") {
            //     this.addTag(id, action, []);
            // }
            else {
                let newItem = {};
                Object.assign(newItem, this.items[id]);
                newItem.id = this.new_identifier;
                this.new_identifier++;
                newItem.selected = false;
                this.prepareItem(newItem, [action]);
            }
        }

        this.relevent_id = null;
        this.relevent_action = null;
    }

    clearAction(){
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
        let filePath;
        let presentationPath;
        if(!this.available_games.includes(game.id)){
            filePath = "js/defaultGame.js";
            presentationPath = "css/defaultGame.css";
        }
        else {
            filePath = game.jsFilePath;
            presentationPath = game.presentationFilePath;
        }

        let head = document.getElementsByTagName("head")[0];
        let gameScript = document.createElement("script");
        gameScript.type = "text/javascript";
        gameScript.src = "/" + filePath;
        let gameLink = document.createElement("link");
        gameLink.rel = "stylesheet";
        gameLink.type = "text/css";
        gameLink.href = "/" + presentationPath;
        head.appendChild(gameLink);
        head.appendChild(gameScript);

        current_game_script = gameScript;
        current_game_css = gameLink;
    }

    setDescriptor(){
        if(!this.descriptor) return;

        let selectedItems = [];
        let selectedIDs = [];
        for(const id in this.items) {
            if(this.items[id].selected === true){
                selectedItems.push(this.items[id]);
                selectedIDs.push(id);
            }
        }

        while(this.descriptor.firstChild){
            this.descriptor.removeChild(this.descriptor.firstChild);
        }
        if(selectedItems.length === 1){
            if(selectedItems[0].use === "tool"){
                let span = document.createElement("span");
                span.textContent = "A " + selectedItems[0].name;
                this.descriptor.appendChild(span);
            }
            else if(selectedItems[0].use === "ingredient" || selectedItems[0].use === "processedItem"){
                let input = document.createElement("input");
                input.setAttribute("type", "number");
                input.setAttribute("class", "item-quantity-input browser-default");
                input.setAttribute("value", selectedItems[0].quantity);
                input.setAttribute("min", "1");
                input.setAttribute("max", "999");
                input.addEventListener("mouseup", (e)=>{this.updateQuantity(selectedIDs[0], e.target.value)});
                input.addEventListener("keyup", (e)=>{this.updateQuantity(selectedIDs[0], e.target.value)});

                let span = document.createElement("span");
                span.textContent = "";
                if(selectedItems[0].unitOfMeasure && selectedItems[0].unitOfMeasure[0])
                    span.textContent += selectedItems[0].unitOfMeasure[0];
                span.textContent += " units of " + selectedItems[0].name + "[s]";

                this.descriptor.appendChild(input);
                this.descriptor.appendChild(span);
            }
            // else if(selectedItems[0].use === "processedItem"){
            //     let span = document.createElement("span");
            //     span.textContent = "A " + selectedItems[0].name + " that has tags:";
            //     for(let tag of selectedItems[0].tags){
            //         span.textContent += " " + tag;
            //     }
            //     this.descriptor.appendChild(span);
            // }
            let remove = document.createElement("span");
            remove.textContent = "Remove this item?";
            remove.setAttribute("style", "cursor: pointer; color: blue; float: right;");
            remove.onclick = ()=>{
                const selected = document.getElementsByClassName("selected");
                this.removeItem(selected[0].id);
                while(this.descriptor.firstChild){
                    this.descriptor.removeChild(this.descriptor.firstChild);
                }
            };
            this.descriptor.appendChild(remove);
        } else if(selectedItems.length === 0){
            //do nothing
        }
    }

    updateQuantity(id, quantity){
        if(!quantity) return;

        let card = document.getElementById(id);
        this.items[id].quantity = parseInt(quantity);

        card.children[1].textContent = quantity + " " + this.items[id].name;
    }

    removeItem(id){
        let item = document.getElementById(id);
        item.parentNode.parentNode.removeChild(item.parentNode);
        delete this.items[id];
        this.updateMenu();
    }

    getTools(){
        let tools = [];
        for(let id in this.items){
            if(this.items[id].use === "tool"){
                tools.push(this.items[id]);
            }
        }
        return tools;
    }

    getIngredients(){
        let ingredients = [];
        for(let id in this.items){
            if(this.items[id].use === "ingredient"){
                ingredients.push(this.items[id]);
            }
        }
        return ingredients;
    }

    addIntermediateStep(item){
        this.intermediate_steps.push(item);
    }

    getIntermediateSteps(){
        return this.intermediate_steps;
    }

    setIntermediateSteps(intermediateSteps){
        this.intermediate_steps = intermediateSteps;
    }

    setSavedStatus(state){
        this.saved = state;
    }

    getSavedStatus(){
        return this.saved;
    }

    getNameByID(id){
        return this.items[id].name;
    }

    setSelected(){
        this.selectedIds = [];
        this.selectedIngredients = [];
        this.selectedTools = [];
        this.selectedIntermediateIngredients = [];
        for(let id in this.items){
            if(this.items[id].selected === true){
                this.selectedIds.push(id);
                if(this.items[id].use === "ingredient")
                    this.selectedIngredients.push(this.items[id].name);
                if(this.items[id].use === "tool")
                    this.selectedTools.push(this.items[id].name);
                if(this.items[id].use === "processedItems"){
                    this.selectedIntermediateIngredients.push(this.items[id].name);
                }
            }
        }
    }
}