/**
 * DOM element[s] relating to recipe persistence
 */
class Prompt {
    constructor(recipe_name, recipe_target, recipe_desc, recipe, cookingBoard){
        this.recipe_name = recipe_name;
        this.recipe_target = recipe_target;
        this.recipe_desc = recipe_desc;
        this.recipe = recipe;
        this.cookingBoard = cookingBoard;
    }

    updateTargets(){
        this.recipe_target.innerHTML = "";
        const targets = this.cookingBoard.getProcessedItems();

        for(const target of targets){
            let opt = document.createElement("option");
            opt.value = target;
            opt.textContent = target;
            this.recipe_target.add(opt);
        }
    }

    getRecipeInfo(){
        let data = {};
        data["name"] = this.recipe_name.value;
        data["target"] = this.recipe_target.options[this.recipe_target.selectedIndex].value;
        data["desc"] = this.recipe_desc.value;
        data["recipe"] = this.recipe.getRecipe();
        data["required"] = this.cookingBoard.getBasicItems();
        return data;
    }

    saveRecipe(){
        let data = this.getRecipeInfo();
        console.log(data);
        //send backend
    }

    publishRecipe(){
        let data = this.getRecipeInfo();
        console.log(data);
        //send backend
    }
}