/**
 * DOM element[s] relating to recipe persistence
 */
class Prompt {
    constructor(recipe_name, recipe, cookingBoard){
        this.recipe_name = recipe_name;
        this.recipe = recipe;
        this.cookingBoard = cookingBoard;
    }

    getRecipeInfo(){
        let data = {};
        data["ingredients"] = this.cookingBoard.getIngredients();
        data["kitchenTools"] = this.cookingBoard.getTools();
        data["recipeName"] = this.recipe_name.value;
        data["subprocedureList"] = this.recipe.getRecipe();
        return data;
    }

    async saveRecipe(){
        let data = this.getRecipeInfo();
        data["isPublished"] = false;

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

        this.cookingBoard.setSavedStatus(true);
        window.location.href = "/home/save/" + data["recipeName"];
    }

    async publishRecipe(){
        let data = this.getRecipeInfo();
        data["isPublished"] = true;

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

        // this.cookingBoard.setSavedStatus(true);
        window.location.href = "/home/" + data["recipeName"];
    }
}