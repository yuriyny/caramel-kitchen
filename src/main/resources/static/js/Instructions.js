/**
 * The DOM element[s] for the recipe instructions.
 */
class Instructions{
    constructor(recipe_ul, recipe){
        this.recipe_ul = recipe_ul;
        this.recipe = recipe;

        this.instructions = [];
    }

    setCommentInput(comment_input){
        comment_input.onkeypress = (e) => {
            if(e.keyCode === 13){
                this.addToRecipe(comment_input.value, null,"comment")
                comment_input.value = "";
            }
        }
    }


    deleteStep(li){
        li.parentNode.removeChild(li);
    }

    addToRecipe(action, target, type="instruction"){
        if(!action) return;

        const li = document.createElement("li");
        if(type === "comment"){li.setAttribute("class", "recipe-step blue lighten-4");}
        else{li.setAttribute("class", "recipe-step");}

        const p = document.createElement("p");
        action = action.charAt(0).toUpperCase() + action.substring(1);
        if(target.split(" ")[0] === "1") target = target.substr(2);

        if(type === "instruction") {
            p.textContent = action + " " + target;
        } else {
            p.textContent = action;
        }
        // p.textContent = p.textContent.split(" ").map(e => e[0].toUpperCase() + e.slice(1)).join(" ")

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons right clear-btn");
        icon.textContent = "clear";
        icon.onclick = () => this.deleteStep(li);

        li.appendChild(p);
        li.appendChild(icon);
        this.recipe_ul.appendChild(li);

        this.instructions.push({"action":action, "target":target, "type":type});
    }

    getRecipe(){
        let recipe = [];
        // for(let li of this.recipe_ul.getElementsByTagName("li")){
        //     let sub = {};
        //     sub["game"] = null;
        //     sub["instructions"] = "placeholder text";
        //     sub["procedureName"] = li.childNodes[0].textContent;
        //     // recipe.push(li.childNodes[0].textContent)
        //     recipe.push(sub);
        // }
        for(const instruction of this.instructions){
            let sub = {};
            sub["game"] = null;
            sub["instructions"] = instruction.action + " " + instruction.target;
            sub["procedureName"] = instruction.action;
            recipe.push(sub);
        }

        return recipe;
    }

    confirmStep(action, target, quantity=1){
        let instruction = "";
        // action = action.charAt(0).toUpperCase() + action.substring(1);
        if(quantity === 1){
            instruction = action + " " + target;
        } else {
            instruction = action + " " + quantity + " " + target;
        }
        // console.log("target instruction is: " + instruction);
        for(let i = 0; i< this.recipe_ul.children.length; i++){
            // console.log("comparing with : " + this.recipe_ul.children[i].textContent);
            if(this.recipe_ul.children[i].textContent.toUpperCase() === instruction.toUpperCase()){
                this.recipe_ul.children[i].classList.add("completed");
                return;
            }
            if(!this.recipe_ul.children[i].classList.contains("completed")){
                break;
            }
        }
        // console.log(this.recipe_ul.children);

        M.toast({html: "That wasn't the correct action though!", displayLength: 1500});
    }
}