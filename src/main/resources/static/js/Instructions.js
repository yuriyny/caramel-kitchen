/**
 * The DOM element[s] for the recipe instructions.
 */
class Instructions{
    constructor(recipe_ul, recipe, intermediate_ingredients){
        this.recipe_ul = recipe_ul;
        this.recipe = recipe;
        this.intermediate_ingredients = intermediate_ingredients;

        this.instructions = [];
        this.index = null;
    }

    setCommentInput(comment_input){
        comment_input.onkeypress = (e) => {
            if(e.keyCode === 13){
                if(this.index = null){ return; }
                const selected_step = document.getElementsByClassName("selected-step")[0];

                const input = document.createElement("span");
                input.textContent = " " + comment_input.value;

                selected_step.children[0].appendChild(input);
                comment_input.value = "";
            }
        }
    }

    deleteStep(li){
        for(let i = 0; i < this.recipe_ul.children.length; i++){
            if(this.recipe_ul.children[i] === li){
                this.instructions.splice(i, 1);
                break;
            }
        }
        li.parentNode.removeChild(li);
        this.setSelectedIndex();

        console.log(this.instructions);
    }

    addToRecipe(action, targets){
        if(!action) return;
        this.clearSelected();

        const li = document.createElement("li");
        li.setAttribute("class", "recipe-step selected-step");
        li.onclick = (e) => {
            let elem = e.target;
            while(!elem.classList.contains("recipe-step")) elem = elem.parentElement;
            if(!elem.classList.contains("selected-step")){
                this.clearSelected();
                elem.classList.add("selected-step");
                this.setSelectedIndex();
            }
        };

        const txt = document.createElement("div");
        txt.setAttribute("style", "max-width: 180px; overflow-wrap: break-word;")

        const p1 = document.createElement("span");
        action = action.charAt(0).toUpperCase() + action.substring(1);
        p1.textContent = action + " ";
        txt.appendChild(p1);

        if(targets.length === 1) {
            let quant = document.createElement("input");
            quant.setAttribute("type", "number");
            quant.setAttribute("class", "item-quantity-recipe-input browser-default");
            quant.setAttribute("value", targets[0].quantity);
            quant.setAttribute("min", "1");
            quant.setAttribute("max", "999");

            let p2 = document.createElement("span");
            p2.textContent = " " + targets[0].name + "[s]";
            if(targets[0].tags && targets[0].tags.length > 0){
                p2.textContent += " with tags, ";
                for(let i = 0; i < targets[0].tags.length ; i++){
                    p2.textContent += targets[0].tags[i];
                    if(i !== targets[0].tags.length - 1) p2.textContent += ", "
                }
                p2.textContent += ";";
            }
            txt.appendChild(quant);
            txt.appendChild(p2);
        } else {
            targets.sort(function(a, b){
                if(a.name < b.name) { return -1; }
                if(a.name > b.name) { return 1; }
                return 0;
            });
            for(let j = 0; j < targets.length; j++){
                let quant = document.createElement("input");
                quant.setAttribute("type", "number");
                quant.setAttribute("class", "item-quantity-recipe-input browser-default");
                quant.setAttribute("value", targets[j].quantity);
                quant.setAttribute("min", "1");
                quant.setAttribute("max", "999");

                let p2 = document.createElement("span");
                p2.textContent = " " + targets[j].name + "[s]";
                if(targets[0].tags && targets[j].tags.length > 0){
                    p2.textContent += " with tags, ";
                    for(let i = 0; i < targets[j].tags.length ; i++){
                        p2.textContent += targets[j].tags[i];
                        if(i !== targets[j].tags.length - 1) p2.textContent += ", "
                    }
                }
                if(j !== targets.length - 1) p2.textContent += " and ";
                txt.appendChild(quant);
                txt.appendChild(p2);
            }
        }

        const remove = document.createElement("i");
        remove.setAttribute("class", "material-icons right hidden-btn");
        remove.textContent = "clear";
        remove.onclick = () => {
            // this.deleteStep(li);
            console.log("instruction removal feature currently redacted");
        }

        li.appendChild(txt);
        li.appendChild(remove);

        // if(this.index === null){
            this.recipe_ul.appendChild(li);
        // } else {
        //     let current_selection = this.recipe_ul.children[this.index];
        //     current_selection.parentNode.insertBefore(li, current_selection.nextSibling);
        // }

        // if(this.index !== null)
        //     this.instructions.splice((this.index + 1), 0, {"action":action, "targets":targets});
        // else
        //     this.instructions.splice(this.index, 0, {"action":action, "targets":targets});
        this.instructions.push({"action":action, "targets":targets});
        this.setSelectedIndex();

        console.log(this.instructions);
    }

    clearSelected(){
        const selected = document.getElementsByClassName("selected-step");
        for(const item of selected){
            item.classList.remove("selected-step");
        }
    }

    setSelectedIndex(){
        for(let i = 0; i< this.recipe_ul.children.length; i++){
            if(this.recipe_ul.children[i].classList.contains("selected-step")){
                this.index = i;
                return;
            }
        }
        this.index = null;
    }

    getRecipe(){
        let recipe = [];

        for(let i = 0; i < this.recipe_ul.children.length; i++){
            let sub = {};
            let text = "";
            for(let j = 0; j < this.recipe_ul.children[i].firstChild.children.length; j++){
                if(this.recipe_ul.children[i].firstChild.children[j].tagName === "INPUT"){
                    text += this.recipe_ul.children[i].firstChild.children[j].value;
                } else {
                    text += this.recipe_ul.children[i].firstChild.children[j].textContent;
                }
            }
            // sub["game"] = null;
            sub["instructions"] = text;
            sub["procedureName"] = this.instructions[i].action;
            sub["targetIngredients"] = this.instructions[i].targets;
            recipe.push(sub);
        }

        // console.log(recipe);
        return recipe;
    }

    confirmStep(action, targets){
        for(let i = 0; i < this.recipe_ul.children.length; i++){
            if(this.recipe_ul.children[i].classList.contains("completed")){
                continue;
            } else {
                if(this.recipe[i].procedureName.toUpperCase() === action.toUpperCase() && this.compareTargetIngredients(this.recipe[i].targetIngredients, targets)){
                    this.recipe_ul.children[i].classList.add("completed");
                    itemBoard.addItem(this.intermediate_ingredients[i], "processedItem", action);
                    scores[i] = true;
                    if (isRecipeCompleted()) $("#finish").removeClass("disabled");
                    return;
                }
                else{
                    break;
                }
            }
        }

        M.toast({html: "That wasn't the correct action though!", displayLength: 1500});
    }

    compareTargetIngredients(targets1, targets2){
        if(!targets1 || !targets2) return false;

        let found = null;
        for(const item1 of targets1){
            found = false;
            for(const item2 of targets2){
                if(item1.name === item2.name && item1.quantity === item2.quantity){
                    found = true;
                    break;
                }
            }
            if(found === false) return false;
        }

        return true;
    }
}