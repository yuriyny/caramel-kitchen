/**
 * The DOM element[s] pertaining to the cooking board with all present
 * utensils and ingredients.
 */
class CookingBoard{
    constructor(board_ul, recipe){
        /** the board element*/
        this.board_ul = board_ul;

        /** the action menu*/
        this.recipe = recipe;

        /** additional info*/
        this.identifier = 0;
        this.items = {};    //let this be a list of hashes with {DOM id : {item name, imageFileUrl, use, tags}}
        this.tools = [];
        this.ingredients = [];
        // this.processedItems = [];
        this.actions = [];
        this.saved = true;

        window.onbeforeunload = ()=> {
            if(this.saved === false) this.pullModal();
        };
    }

    /** MENU ADD ITEM---------------------------------*/
    addItem(item, use, tag=[]){
        // console.log(use);
        let record = {};
        record["name"] = item.name;
        if(tag.length > 0) record["tags"] = [tag];
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

        const card_menu = document.createElement("div");
        card_menu.setAttribute("class", "card-menu");

        const card_menu_ul = document.createElement("ul");
        card_menu_ul.setAttribute("id", "card-menu-ul");

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

        card_menu.appendChild(card_menu_ul);
        card_content.appendChild(content);
        card_img.appendChild(img);
        card.appendChild(card_img);
        card.appendChild(card_content);
        card.appendChild(card_menu);
        wrapper.appendChild(card);
        this.board_ul.appendChild(wrapper);

        this.items[this.identifier] = record;
        this.identifier++;

        this.updateMenu();
        console.log(this.items);
    }

    addTag(itemKey, tag) {
        if (this.items[itemKey].tags.includes(tag)) return;

        document.getElementById(itemKey).childNodes[1].firstChild.textContent += ", " + tag;

        this.items[itemKey].tags.push(tag);
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
    }

    updateMenu(){
        let menu_ul;
        for(const key in this.items){
            // const id = "#" + key;
            if(this.items[key].use === "tool") continue;

            const card = document.getElementById(key);
            console.log(card);
            menu_ul = card.childNodes[2].firstChild;
            while(menu_ul.firstChild){
                menu_ul.removeChild(menu_ul.firstChild);
            }

            for(const action of this.actions){
                const li = document.createElement("li");
                li.setAttribute("class", "menu-option");
                li.setAttribute("oncontextmenu", "return false");
                li.textContent = action;

                li.onclick = (e) => {
                    // let item_name = e.target.parentNode.parentNode.previousSibling.firstChild.innerHTML;

                    let step = li.innerHTML;
                    for (const tag of this.items[key].tags) step += " '" + tag + "'";
                    step += " " + this.items[key].name;
                    this.recipe.addToRecipe(step);

                    if(this.items[key].use === "processedItem"){
                        this.addTag(key, li.innerHTML);
                    } else {
                        let newItem = {"name": this.items[key].name, "imageFileUrl": this.items[key].img};
                        this.addItem(newItem, "processedItem", li.innerHTML);
                    }

                    this.saved = false;
                }
                menu_ul.appendChild(li);
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

    setSavedStatus(state){
        this.saved = state;
    }

    getSavedStatus(){
        return this.saved;
    }
}