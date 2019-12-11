/**
 * DOM element[s] relating to recipe persistence
 */
class Prompt {
    constructor(recipe_name, recipe_image, recipe, cookingBoard){
        this.recipe_name = recipe_name;
        this.recipe_image = recipe_image;
        this.recipe = recipe;
        this.cookingBoard = cookingBoard;
		
		this.data = {};
    }

    getRecipeInfo(){
        this.data["id"] = this.cookingBoard.recipeId;
        this.data["ingredients"] = this.cookingBoard.getIngredients();
        this.data["kitchenTools"] = this.cookingBoard.getTools();
        this.data["recipeName"] = this.recipe_name.value;
        this.data["intermediateIngredients"] = this.cookingBoard.getIntermediateSteps();
        this.data["subprocedureList"] = this.recipe.getRecipe();
    }

    async setRecipeImage() {
        if (this.recipe_image.files.length == 1) {
            let form_data = new FormData();
            form_data.append('file', this.recipe_image.files[0]);
            const imageKey = await fetch("/upload-image", {
                  method: "POST",
                  body: form_data
            })
                  .then((response) => response.json())
                  .then((response) => response.message)
                  .catch(e => {console.log("err ", e)});
			this.data["recipeImage"] = imageKey;

        }
    }

    async saveRecipe(){

        if(document.getElementsByClassName("preparingItem").length > 0){
            M.toast({html:"Define the current prepared item first!", displayLength:1000});
            return;
        }

        this.getRecipeInfo();
        this.data["isPublished"] = false;

        const send = await fetch("/save-recipe", {
            method: "POST",
            body: JSON.stringify(this.data),
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        })
            .then(response => "success")
            .catch((e)=>{console.log("err " + e)});

        this.cookingBoard.setSavedStatus(true);
        window.location.href = "/home/save/" + this.data["recipeName"];
    }

    async publishRecipe(){
        if(document.getElementsByClassName("preparingItem").length > 0){
            M.toast({html:"Define the current prepared item first!", displayLength:1000});
            return;
        }

        this.getRecipeInfo();
        this.data["isPublished"] = true;

        if(!this.data["recipeName"]){
            console.log("missing name");
            return;
        }

        const send = await fetch("/create-recipe", {
            method: "POST",
            body: JSON.stringify(this.data),
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        })
            .then(response => "success")
            .catch(e => {console.log("err " + e)});

        // this.cookingBoard.setSavedStatus(true);
        window.location.href = "/home/" + this.data["recipeName"];
    }
}