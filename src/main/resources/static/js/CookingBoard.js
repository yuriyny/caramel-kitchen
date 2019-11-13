/**
 * The DOM element[s] pertaining to the cooking board with all present
 * utensils and ingredients.
 */
class CookingBoard{
    constructor(board_ul, menu, menu_ul, recipe){
        /** the board element*/
        this.board_ul = board_ul;

        /** the action menu*/
        this.menu = menu;
        this.menu_ul = menu_ul;
        this.recipe = recipe;

        /** additional info*/
        this.identifier = 0;
        this.items = {};    //let this be a list of hashes with {DOM id : {item name, imageFileUrl, use, tags}}
        this.tools = [];
        this.ingredients = [];
        // this.processedItems = [];
        this.actions = [];

        /** misc*/
        this.menuVisible = false;
        this.focus_item = null;

        this.order = 0;
        window.addEventListener("click", e => {
            if(this.menuVisible && this.order === 0)
                this.toggleMenu("hide");
            this.order = 0;
        });
    }

    /** MENU ADD ITEM---------------------------------*/
    addItem(item, use, tag=[]){
        // console.log(use);
        let record = {};
        record["name"] = item.name;
        if(tag) record["tags"] = [tag];
        else record["tags"] = [];
        record["use"] = use;

        const wrapper = document.createElement("div");
        wrapper.setAttribute("class", "col s4 m3 l2");

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
            img.setAttribute("src", "images/placeholder.png");    //make a placeholder.png later
            record["img"] = "images/placeholder.png";
        }

        const card_content = document.createElement("div");
        card_content.setAttribute("class", "card-content center");

        const content = document.createElement("p");
        content.textContent = item.name;

        if(use === "ingredient"){
            card.classList.add("ingredient");
            this.attachMenu(card);
            this.addIngredient(item);
        }
        else if(use === "tool"){
            this.addTool(item);
        }
        else if(use === "processedItem"){
            card.classList.add("processedItem");
            this.attachMenu(card);
            content.textContent += ": " + tag;
        } else { console.log("item use not defined"); }

        card_content.appendChild(content);
        card_img.appendChild(img);
        card.appendChild(card_img);
        card.appendChild(card_content);
        wrapper.appendChild(card);
        this.board_ul.appendChild(wrapper);

        this.items[this.identifier] = record;
        this.identifier++;

        console.log(this.items);
    }

    addTag(item, tag){
        if(item.tags.includes(tag)) return;
        this.focus_item.childNodes[1].textContent += ", " + tag;

        item.tags.push(tag);
    }

    /** ACTION MENU FUNCTIONS --------------------------------*/
    /**moves action menu object to a location on page */
    setPosition({x, y}){
        this.menu.style.left = `${x}px`;
        this.menu.style.top = `${y}px`;
        this.toggleMenu("show");
        this.order += 1;
    }

    /**toggle visibility of action menu */
    toggleMenu(state){
        if(state === "show"){
            this.menuVisible = true;
            this.menu.style.display = "block";
        }
        else if(state === "hide"){
            this.menuVisible = false;
            this.menu.style.display = "none";
        }
    }

    /**adds action menu feature to an item on page */
    attachMenu(item){
        // if(this.utensils.includes(item.textContent)) return;
        item.onclick = e => {
            e.preventDefault();
            const origin = {
                x: e.pageX,
                y: e.pageY
            };
            this.setPosition(origin);
            this.focus_item = item;
            return false;
        };
    }

    /**add to ingredient list*/
    addIngredient(item){
        // if(!this.ingredients.includes(item.name)) this.ingredients.push(item);
        for(const ingredient of this.ingredients){
            if(ingredient.name === item.name) return;
        }
        this.ingredients.push(item);
    }

    /**add to tools list as known by action menu */
    addTool(item){
        // if(!this.tools.includes(name)) this.tools.push(name);
        let newTool = true;
        for(const tool of this.tools){
            if(tool.name === item.name) return;
        }
        this.tools.push(item);

        console.log(item);
        console.log(item.actions);

        for(let action of item.actions){
            if(!this.actions.includes(action))
                this.actions.push(action)
        }

        // const path = "/tool/" + name;
        // const actions = await fetch(path, {
        //     method: "GET",
        //     contentType: "text/plain"
        // })
        //     .then(response => response.json())
        //     .catch((e)=>{console.log("err " + e)});
        //
        // for(let action of actions){
        //     if(!this.actions.includes(action))
        //         this.actions.push(action)
        // }

        this.updateMenu();
    }

    /**get item that action menu is currently showing for */
    getFocusItem(){
        return this.items[this.focus_item.id];
    }

    updateMenu(){
        while(this.menu_ul.firstChild){
            this.menu_ul.removeChild(this.menu_ul.firstChild);
        }

        for(const action of this.actions){
            const li = document.createElement("li");
            li.setAttribute("class", "menu-option");
            li.setAttribute("oncontextmenu", "return false");
            li.textContent = action;

            li.onclick = () => {
                let item = this.getFocusItem();
                this.recipe.addToRecipe(`${li.innerHTML} ${item.name}`);

                if(item.use === "processedItem"){
                    this.addTag(item, li.innerHTML);
                } else {
                    let newItem = {"name": item.name, "imageFileUrl": item.img};
                    this.addItem(newItem, "processedItem", li.innerHTML);
                }
            }
            this.menu_ul.appendChild(li);
        }

        if(!this.menu_ul.firstChild){
            const li = document.createElement("li");
            li.setAttribute("class", "menu-option noSelect");
            li.setAttribute("oncontextmenu", "return false");
            li.textContent = "No actions available";
            this.menu_ul.appendChild(li);
        }
    }

    /** MISC------------------------------------*/
    getTools(){
        console.log(this.tools);
        return this.tools;
    }

    getIngredients(){
        console.log(this.ingredients);
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
}