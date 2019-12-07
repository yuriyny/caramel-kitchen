/**
 *
 */
class RecipeInteract{
   constructor(recipe_ul){
       this.recipe_ul = recipe_ul;

       this.recipes = {};
   }

   async loadCreatedRecipes(){
       const data = await fetch("/get-all-user-recipes", {
           method: "GET",
           contentType: "text/plain"
       })
           .then(response => response.json())
           .catch((e)=>{console.log("err " + e)});

       console.log(data);
       for(const recipe of data){
           this.addRecipe(recipe);
           this.recipes[recipe.recipeName] = recipe;
       }
   }

    async loadInprogressRecipes(){
           const data = await fetch("/get-all-user-in-progress-recipes", {
               method: "GET",
               contentType: "text/plain"
           })
               .then(response => response.json())
               .catch((e)=>{console.log("err " + e)});

           console.log(data);
           for(const game of data){
               this.addInprogressRecipe(game);
               this.recipes[recipe.recipeName] = game.recipe;
           }
       }

    async loadCompletedRecipes(){
           const data = await fetch("/get-user-completed-recipes", {
               method: "GET",
               contentType: "text/plain"
           })
               .then(response => response.json())
               .catch((e)=>{console.log("err " + e)});

           console.log(data);
           for(const game of data){
               this.addCompletedRecipe(game);
               this.recipes[recipe.recipeName] = game.recipe;
           }
       }

   addRecipe(recipe){
       const li = document.createElement("li");
       const col_header = document.createElement("div");
       if (recipe.isPublished) {
            col_header.setAttribute("class", "collapsible-header btn-large waves-effect waves-light custom-collapsible-btn-published");
       } else {
            col_header.setAttribute("class", "collapsible-header btn-large waves-effect waves-light custom-collapsible-btn-saved");
       }

       col_header.textContent = recipe.recipeName;

       const col_body = document.createElement("div");
       col_body.setAttribute("class", "collapsible-body");

       const rating = document.createElement("p");
       rating.textContent = "Rating: " + recipe.rating;

       const edit = document.createElement("a");
       edit.textContent = "Modify";
       edit.setAttribute("href", "/edit/"+ recipe.id);

       const remove = document.createElement("a");
       remove.textContent = "Remove";
       remove.setAttribute("href", "#");
       remove.onclick = (e) => {
           if(confirm('Are you sure you want to remove selected recipe?')){
                this.removeRecipe(e.target.parentNode.parentNode);
           }
       }

       col_body.appendChild(rating);
       col_body.appendChild(edit);
       col_body.appendChild(document.createElement("div"));
       col_body.appendChild(remove);
       li.appendChild(col_header);
       li.appendChild(col_body);
       this.recipe_ul.appendChild(li);
   }
    addInprogressRecipe(game){
           const recipe = game.recipe;
           const li = document.createElement("li");
           const col_header = document.createElement("div");
           col_header.setAttribute("class", "collapsible-header btn-large waves-effect waves-light custom-collapsible-btn");
           col_header.textContent = recipe.recipeName;

           const col_body = document.createElement("div");
           col_body.setAttribute("class", "collapsible-body");

           const rating = document.createElement("p");
           rating.textContent = "Rating: " + recipe.rating;

           const creator = document.createElement("p");
           creator.textContent = "Creator: " + recipe.creator;

           col_body.appendChild(creator);
           col_body.appendChild(rating);
           li.appendChild(col_header);
           li.appendChild(col_body);
           this.recipe_ul.appendChild(li);
       }

   addCompletedRecipe(game){
              const recipe = game.recipe;
              const li = document.createElement("li");
              const col_header = document.createElement("div");
              col_header.setAttribute("class", "collapsible-header btn-large waves-effect waves-light custom-collapsible-btn");
              col_header.textContent = recipe.recipeName;

              const col_body = document.createElement("div");
              col_body.setAttribute("class", "collapsible-body");

              const rating = document.createElement("p");
              rating.textContent = "Rating: " + recipe.rating;

              const creator = document.createElement("p");
              creator.textContent = "Creator: " + recipe.creator;

              const score = document.createElement("p");
              creator.textContent = "Score: " + game.score;

              col_body.appendChild(creator);
              col_body.appendChild(rating);
              li.appendChild(col_header);
              li.appendChild(col_body);
              this.recipe_ul.appendChild(li);
          }
   async removeRecipe(item){
       const recipe_name = item.firstChild.textContent;
       const data = this.recipes[recipe_name];

       const send = await fetch("/delete-recipe", {
           method: "POST",
           body: JSON.stringify(data),
           headers: {
               "Content-Type": "application/json",
               "Accept": "application/json"
           }
       })
           .then(response=>"success")
           .catch(e=>{console.log("err " + e)});
       console.log(send);

       item.parentNode.removeChild(item);
   }
}