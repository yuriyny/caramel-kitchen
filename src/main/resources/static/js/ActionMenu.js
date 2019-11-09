/**
 * The DOM element[s] for my custom context menu when interacting
 * with cooking board items.
 */
class ActionMenu{
    constructor(menu, menu_ul, recipe){
        this.menu = menu;
        this.menu_ul = menu_ul;
        this.recipe = recipe;

        this.utensils = [];
        this.actions = [];

        this.menuVisible = false;
        this.focus_item = null;

        this.order = 0;
        window.addEventListener("click", e => {
            if(this.menuVisible && this.order === 0)
                this.toggleMenu("hide");
            this.order = 0;
        });
    }

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

    /**add to utensils list as known by action menu */
    addUtensil(name){
        if(!this.utensils.includes(name)) this.utensils.push(name);
    }

    /**get item that action menu is currently showing for */
    getFocusItem(){
        return this.focus_item;
    }

    /**actions should be retrieved from http request later */
    getActions(){
        const tempActionList = { "knife": "Chop", "salt": "Salt", "rolling pin": "Flatten", "kettle": "Boil"}

        let actions = [];
        for(let utensil of this.utensils){
            if(tempActionList[utensil]) actions.push(tempActionList[utensil])
        }
        return actions
    }

    updateMenu(){   //REDO THIS LATER
        //might not need this since im not sure items are removable
        while(this.menu_ul.firstChild){
            this.menu_ul.removeChild(this.menu_ul.firstChild);
        }
        this.actions = [];

        const actions = this.getActions();

        for(const action of actions){
            const li = document.createElement("li");
            li.setAttribute("class", "menu-option");
            li.setAttribute("oncontextmenu", "return false");
            li.textContent = action;

            li.onclick = () => this.recipe.addStep(`${li.innerHTML} ${this.focus_item.children[1].innerText}`);
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
}