// /**
//  * The DOM element[s] for my custom context menu when interacting
//  * with cooking board items.
//  */
// class ActionMenu{
//     constructor(menu, menu_ul, recipe){
//         this.menu = menu;
//         this.menu_ul = menu_ul;
//         this.recipe = recipe;
//
//         this.tools = [];
//         this.actions = [];
//
//         this.menuVisible = false;
//         this.focus_item = null;
//
//         this.order = 0;
//         window.addEventListener("click", e => {
//             if(this.menuVisible && this.order === 0)
//                 this.toggleMenu("hide");
//             this.order = 0;
//         });
//     }
//
//     /**moves action menu object to a location on page */
//     setPosition({x, y}){
//         this.menu.style.left = `${x}px`;
//         this.menu.style.top = `${y}px`;
//         this.toggleMenu("show");
//         this.order += 1;
//     }
//
//     /**toggle visibility of action menu */
//     toggleMenu(state){
//         if(state === "show"){
//             this.menuVisible = true;
//             this.menu.style.display = "block";
//         }
//         else if(state === "hide"){
//             this.menuVisible = false;
//             this.menu.style.display = "none";
//         }
//     }
//
//     /**adds action menu feature to an item on page */
//     attachMenu(item){
//         // if(this.utensils.includes(item.textContent)) return;
//         item.onclick = e => {
//             e.preventDefault();
//             const origin = {
//                 x: e.pageX,
//                 y: e.pageY
//             };
//             this.setPosition(origin);
//             this.focus_item = item;
//             return false;
//         };
//     }
//
//     /**add to tools list as known by action menu */
//     async addTool(name){
//         if(!this.tools.includes(name)) this.tools.push(name);
//         // const path = "/tool/" + name;
//         console.log("I am now calling /tool/knife");
//         const actions = await fetch("/tool/knife", {
//             method: "GET",
//             contentType: "text/plain"
//         })
//             .then(response => response.json())
//             .catch((e)=>{console.log("err " + e)});
//
//         console.log("i have finished calling /tool/knife and got: " + actions);
//         // console.log(typeof actions);
//         // console.log(actions);
//         for(let action of actions){
//             if(!this.actions.includes(action))
//                 this.actions.push(action)
//         }
//
//         this.updateMenu();
//     }
//
//     /**get item that action menu is currently showing for */
//     getFocusItem(){
//         return this.focus_item;
//     }
//
//     updateMenu(){
//         while(this.menu_ul.firstChild){
//             this.menu_ul.removeChild(this.menu_ul.firstChild);
//         }
//
//         for(const action of this.actions){
//             const li = document.createElement("li");
//             li.setAttribute("class", "menu-option");
//             li.setAttribute("oncontextmenu", "return false");
//             li.textContent = action;
//
//             li.onclick = () => this.recipe.addToRecipe(`${li.innerHTML}`, `${this.focus_item.children[1].innerText}`);
//             this.menu_ul.appendChild(li);
//         }
//     }
// }