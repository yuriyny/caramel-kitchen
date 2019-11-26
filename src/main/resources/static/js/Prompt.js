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
        //recipe requires from frontend
        //  ingredients, kitchen tools, isinProgress, recipe name, and subprocedure list
        let data = {};
        // data["target"] = this.recipe_target.options[this.recipe_target.selectedIndex].value;
        // data["desc"] = this.recipe_desc.value;
        data["ingredients"] = this.cookingBoard.getIngredients();
        data["kitchenTools"] = this.cookingBoard.getTools();
        data["recipeName"] = this.recipe_name.value;
        data["subprocedureList"] = this.recipe.getRecipe();
        return data;
    }

    async saveRecipe(){
        let data = this.getRecipeInfo();
        data["isPublished"] = false;
        console.log(data);

        const send = await fetch("/create-recipe", {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        })
            .then(response => "success")
            .catch((e)=>{console.log("err " + e)});
        console.log(send);

        this.cookingBoard.setSavedStatus(true);
        window.location.href = "/home/save/" + data["recipeName"];
    }

    async publishRecipe(){
        let data = this.getRecipeInfo();
        data["isPublished"] = true;
        console.log(data);

        if(!data["recipeName"]){
            console.log("missing name");
            return;
        }

        const send = await fetch("/create-recipe", {
            method: "POST",
            body: JSON.stringify(data),
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        })
            .then(response => "success")
            .catch(e => {console.log("err " + e)});

        console.log(send);
        // this.cookingBoard.setSavedStatus(true);
        window.location.href = "/home/" + data["recipeName"];
    }
}