/**
 * The DOM element[s] for my custom context menu when interacting 
 * with cooking board items.
 */
class ActionMenu{
    constructor(menuElement, menu_ul, lab_instructions){
        this.menu = menuElement;
        this.menu_ul = menu_ul;
        this.lab_instructions = lab_instructions;
        this.utensils = [];
        this.actions = [];
        this.menuVisible = false;
        this.focus_item = null;
        this.order = 0;
        window.addEventListener("click", e => {
            // console.log("window has detected a click! MenuVisible:" + this.menuVisible);
            if(this.menuVisible && this.order === 0)
                this.toggleMenu("hide"); 
            this.order = 0;
        });
    }

    setPosition({x, y}){
        this.menu.style.left = `${x}px`;
        this.menu.style.top = `${y}px`;
        this.toggleMenu("show");
        this.order += 1;
    }

    toggleMenu(state){
        if(state === "show"){
            this.menuVisible = true;
            menu.style.display = "block";
        }
        else if(state === "hide"){
            this.menuVisible = false;
            menu.style.display = "none";
        }
    }

    addMenuContext(item){
        if(this.utensils.includes(item.textContent)) return;
        // item.addEventListener("contextmenu", e => {
        //     e.preventDefault();
        //     const origin = {
        //         x: e.pageX,
        //         y: e.pageY
        //     };
        //     this.setPosition(origin);
        //     this.focus_item = item;
        //     return false;
        // });
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

    addUtensil(name){
        this.utensils.push(name);
    }

    getFocusItem(){
        return this.focus_item;
    }

    updateMenu(){   //REDO THIS LATER
        while(this.menu_ul.firstChild){
            this.menu_ul.removeChild(this.menu_ul.firstChild);
        }
        this.actions = [];

        const random = Math.floor(Math.random() * 3) + 1;
        // let randomActions = ["Chop", "Boil", "Beat", "Fry"];    //this is just random actions
        // randomActions.sort( function() { return 0.5 - Math.random() } );    //formally should be gotten from other tools
        let tempActionList = { "knife": "Chop", "blender": "Blend", "rolling pin": "Flatten", "peeler": "Peel"}

        for(let tool in tempActionList){
            if(this.utensils.includes(tool)){
                const li = document.createElement("li");
                li.setAttribute("class", "menu_option");
                li.setAttribute("oncontextmenu", "return false");
                li.onclick = () => this.lab_instructions.addLabStep(`${li.innerHTML} ${this.focus_item.innerHTML}`);
                li.textContent = tempActionList[tool];
                // this.actions.push(tempActionList[tool]);

                this.menu_ul.appendChild(li);
            }
        }

        if(!this.menu_ul.firstChild){
            const li = document.createElement("li");
            li.setAttribute("class", "menu_option noSelect");
            li.setAttribute("oncontextmenu", "return false");
            li.textContent = "No actions available";
            this.menu_ul.appendChild(li);
        }
    }
}