/**
 *
 */
class RecipeInteract{
   constructor(recipe_ul){
       this.recipe_ul = recipe_ul;

       this.recipes = {};
   }

   async loadRecipes(){
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

   addRecipe(recipe){
       const li = document.createElement("li");
       const col_header = document.createElement("div");
       col_header.setAttribute("class", "collapsible-header btn-large waves-effect waves-light custom-collapsible-btn");
       col_header.textContent = recipe.recipeName;

       const col_body = document.createElement("div");
       col_body.setAttribute("class", "collapsible-body");

       const rating = document.createElement("p");
       rating.textContent = "Rating: " + recipe.rating;

       const remove = document.createElement("a");
       remove.textContent = "Remove";
       remove.setAttribute("href", "#");
       remove.onclick = (e) => {
           this.removeRecipe(e.target.parentNode.parentNode);
       }

       const modify = document.createElement("a");
       modify.textContent = "Modify";
       modify.setAttribute("href", "#");

       col_body.appendChild(rating);
       col_body.appendChild(remove);
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