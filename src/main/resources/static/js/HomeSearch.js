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
        this.search_input.addEventListener("keyup", (e)=>{
            if(e.keyCode === 13){ this.search_btn.click(); }
        })
    }

    async searchQuery(){
        const keyword = this.search_input.value;
        const category = this.search_category.options[this.search_category.selectedIndex].value;
        if(category === "creator") return;
        if(keyword === ""){
            this.searchAllQuery();
            return;
        }

        this.clearSearchUl();

        const results = await fetch("/search-recipe-list", {
            method: "POST",
            body: keyword,
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        console.log(results);
        for(let result of results) {
            if (category === "recipe") {
                this.createRecipeResultLi(result);
            } else if (category === "creator") {
                this.createCreatorResultLi(result)
            }
        }
    }

    async searchAllQuery(){
        this.clearSearchUl();

        const results = await fetch("/recipe-list", {
            method: "GET",
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        for(let result of results) {
            this.createRecipeResultLi(result);
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
        title.setAttribute("href", "play/"+result.id);
        title.textContent = result.recipeName;

        const info = document.createElement("p");
        info.textContent = result.creator;

        const rating = document.createElement("p");
        rating.setAttribute("class", "secondary-content");
        rating.textContent = "Rating: " + result.rating;

        li.appendChild(icon);
        li.appendChild(title);
        li.appendChild(info);
        li.appendChild(rating);

        this.search_ul.appendChild(li);
    }

    createCreatorResultLi(result){
    }

}