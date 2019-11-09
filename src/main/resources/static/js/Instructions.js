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
                this.addComment(comment_input.value)
                comment_input.value = "";
            }
        }
    }

    addComment(comment){
        if(!comment) return;

        const li = document.createElement("li");
        li.setAttribute("class", "collection-item blue lighten-4");

        const div = document.createElement("div");
        div.textContent = comment;

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons right clear-btn");
        icon.textContent = "clear";
        icon.onclick = () => this.deleteStep(li);

        div.appendChild(icon);
        li.appendChild(div);
        this.recipe_ul.appendChild(li);
    }

    deleteStep(li){
        li.parentNode.removeChild(li);
    }

    addStep(step){
        const li = document.createElement("li");
        li.setAttribute("class", "collection-item");

        const div = document.createElement("div");
        div.textContent = step;

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons right clear-btn");
        icon.textContent = "clear";
        icon.onclick = () => this.deleteStep(li);

        div.appendChild(icon);
        li.appendChild(div);
        this.recipe_ul.appendChild(li);
    }
}