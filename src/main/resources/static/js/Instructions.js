/**
 * The DOM element[s] for the recipe instructions.
 */
class Instructions{
    constructor(recipe_ul, recipe){
        this.recipe_ul = recipe_ul;
        this.recipe = recipe;

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

    addToRecipe(action, targets, initial_quantity=1){
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

        const quant = document.createElement("input");
        quant.setAttribute("type", "number");
        quant.setAttribute("class", "item-quantity-recipe-input browser-default");
        quant.setAttribute("value", initial_quantity.toString(10));
        quant.setAttribute("min", "1");
        quant.setAttribute("max", "999");

        const p2 = document.createElement("span");
        if(targets.length === 1) {
            p2.textContent = " " + targets[0].name + "[s];";
        } else {
            //somethign else for multiple target ingredients
        }

        const remove = document.createElement("i");
        remove.setAttribute("class", "material-icons right hidden-btn");
        remove.textContent = "clear";
        remove.onclick = () => this.deleteStep(li);

        txt.appendChild(p1);
        txt.appendChild(quant);
        txt.appendChild(p2);
        li.appendChild(txt);
        li.appendChild(remove);

        if(this.index === null){
            this.recipe_ul.appendChild(li);
        } else {
            let current_selection = this.recipe_ul.children[this.index];
            current_selection.parentNode.insertBefore(li, current_selection.nextSibling);
        }

        if(this.index !== null)
            this.instructions.splice((this.index + 1), 0, {"action":action, "targets":targets});
        else
            this.instructions.splice(this.index, 0, {"action":action, "targets":targets});
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

    confirmStep(action, targets, quantity=1){
        for(let i = 0; i < this.recipe_ul.children.length; i++){
            if(this.recipe_ul.children[i].classList.contains("completed")){
                continue;
            } else {
                // console.log(this.recipe[i].procedureName.toUpperCase());
                // console.log(action.toUpperCase());
                // console.log(this.recipe[i].targetIngredients);
                // console.log(targets);
                if(this.recipe[i].procedureName.toUpperCase() === action.toUpperCase() && this.compareTargetIngredients(this.recipe[i].targetIngredients, targets)){
                    this.recipe_ul.children[i].classList.add("completed");
                    this.scores[i] = true;
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
        let found = null;
        for(const item1 of targets1){
            found = false;
            for(const item2 of targets2){
                if(item1.name === item2.name){
                    found = true;
                    break;
                }
            }
            if(found === false) return false;
        }

        return true;
    }
}