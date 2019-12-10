/**
 * The DOM element[s] regarding lab and user search in home.
 */
class HomeSearch{
    constructor(search_input, search_category, search_results_ul, search_btn){
        this.search_input = search_input;
        this.search_category = search_category;
        this.search_ul = search_results_ul;
        this.search_btn = search_btn;
        
        this.search_btn.onclick = () => this.searchQuery();
        // this.search_input.addEventListener("keyup", (e)=>{
        //     if(e.keyCode === 13){ this.search_btn.click(); }
        // })
        this.search_input.addEventListener("input", ()=>{ this.searchQuery(); });
    }

    async searchQuery(){
        const keyword = this.search_input.value;
        const category = this.search_category.options[this.search_category.selectedIndex].value;
        // if(category === "creator") return;
        if(keyword === ""){
            this.searchAllQuery(category);
            return;
        }

        this.clearSearchUl();

        if (category ==="recipe"){
            const recipeResults = await fetch("/search-recipe-list", {
                method: "POST",
                body: keyword,
                contentType: "text/plain"
            })
                .then(response => response.json())
                .catch((e)=>{console.log("err " + e)});
            for(let result of recipeResults){
                this.createRecipeResultLi(result);
            }
        }else{
            const userResults = await fetch("/search-user", {
                method: "POST",
                body: keyword,
                contentType: "text/plain"
            })
                .then(response => response.json())
                .catch((e)=>{console.log("err " + e)});
            for(let result of userResults){
                this.createCreatorResultLi(result)
            }
        }
    }

    async searchAllQuery(category){
        this.clearSearchUl();
        if (category==="recipe") {
            const recipeResults=await fetch("/recipe-list", {
                method: "GET",
                contentType: "text/plain"
            })
                .then(response => response.json())
                .catch((e) => {
                    console.log("err " + e)
                });
            console.log(recipeResults);
            for (let result of recipeResults) {
                let recipe_rating_data = await fetch("/rating/" + result.id, {
                                                                           method: "GET"
                                                                       })
                                                                       .then(response => response.json())
                                                                       .catch((e)=>{console.log("err " + e)});
                result['rating'] = recipe_rating_data['score'];
                this.createRecipeResultLi(result);
            }
        }else{
            const userResults= await fetch("/user-list", {
                method: "GET",
                contentType: "text/plain"
            })
                .then(response => response.json())
                .catch((e) => {
                    console.log("err " + e)
                });
            for (let result of userResults) {
                this.createCreatorResultLi(result);
            }
        }
    }

    clearSearchUl(){
        while(this.search_ul.firstChild){
            this.search_ul.removeChild(this.search_ul.firstChild)
        }
    }

    createRecipeResultLi(result){
        const li = document.createElement("li");
        li.setAttribute("class", "collection-item avatar");

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons circle orange light-3");
        icon.textContent = "format_list_bulleted";

        const title = document.createElement("a");
        title.setAttribute("class", "title");
        title.setAttribute("href", "/play/"+result.id);
        title.textContent = result.recipeName;

        const info = document.createElement("p");
        info.textContent = result.creator;

        const rating = document.createElement("p");
        rating.setAttribute("class", "secondary-content");
        if (!result.rating) {
                   result.rating = "-";
        }
        rating.textContent = "Rating: " + result.rating;

        li.appendChild(icon);
        li.appendChild(title);
        li.appendChild(info);
        li.appendChild(rating);

        this.search_ul.appendChild(li);
    }

    createCreatorResultLi(result){
        const li = document.createElement("li");
        li.setAttribute("class", "collection-item avatar");

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons circle orange light-3");
        icon.textContent = "format_list_bulleted";

        const title = document.createElement("a");
        title.setAttribute("class", "title");
        title.setAttribute("href", "#");
        title.textContent = result.username;

        li.appendChild(icon);
        li.appendChild(title);

        this.search_ul.appendChild(li);
    }

}