/**
 * The DOM element[s] for the recipe instructions.
 */
class Instructions{
    constructor(recipe_ul, recipe){
        this.recipe_ul = recipe_ul;
        this.recipe = recipe;
    }

    setCommentInput(comment_input){
        comment_input.onkeypress = (e) => {
            if(e.keyCode === 13){
                this.addToRecipe(comment_input.value, "comment")
                comment_input.value = "";
            }
        }
    }


    deleteStep(li){
        li.parentNode.removeChild(li);
    }

    addToRecipe(step, type="instruction"){
        if(!step) return;

        const li = document.createElement("li");
        if(type === "comment"){li.setAttribute("class", "collection-item recipe-step blue lighten-4");}
        else{li.setAttribute("class", "collection-item recipe-step");}

        const p = document.createElement("p");
        p.textContent = step;

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons right clear-btn");
        icon.textContent = "clear";
        icon.onclick = () => this.deleteStep(li);

        li.appendChild(p);
        li.appendChild(icon);
        this.recipe_ul.appendChild(li);
    }

    getRecipe(){
        let recipe = [];
        for(let li of this.recipe_ul.getElementsByTagName("li")){
            let sub = {};
            sub["game"] = null;
            sub["instructions"] = "placeholder text";
            sub["procedureName"] = li.childNodes[0].textContent;
            // recipe.push(li.childNodes[0].textContent)
            recipe.push(sub);
        }

        return recipe;
    }
}