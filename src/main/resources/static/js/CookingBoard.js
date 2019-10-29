/**
 * The DOM element[s] pertaining to the cooking board with all present 
 * utensils and ingredients.
 */
class CookingBoard{
    constructor(boardElement, menuElement){
        this.listElement = boardElement;
        this.contextMenu = menuElement;
        /* items consist of utensils and ingredients */
        this.items = [];
        this.utensils  = [];
        this.ingredients = [];
    }

    createItem(name, ingredient){   //maybe have some way to identify an active_item as either ingredient or utensil
        const item = document.createElement("div");
        if(ingredient === "ingredient"){
            item.setAttribute("class", "active_item ingredient");
        } else {
            item.setAttribute("class", "active_item")
        }
        item.textContent = name;
        return item;
    }

    clear(){
        while(this.listElement.firstChild){
            this.listElement.removeChild(this.listElement.firstChild)
        }
        this.items = [];
        this.utensils = [];
        this.ingredients = [];
    }

    addItem(name){
        //code to be get item image and category (ingredient/utensil) should be here
        const tempToolList = ["knife", "peeler", "rolling pin", "blender"];
        let category = "ingredient";
        if(tempToolList.includes(name)){
            this.contextMenu.addUtensil(name);
            category = "utensil";
        }

        const li = document.createElement("li");
        const item = this.createItem(name, category);
        this.contextMenu.addMenuContext(item);
        li.appendChild(item);
        this.listElement.appendChild(li);
        this.items.push(name);
        if(category === "ingredient"){
            this.ingredients.push(name);
        } else {
            this.utensils.push(name);
        }
        
        this.contextMenu.updateMenu();
    }
}