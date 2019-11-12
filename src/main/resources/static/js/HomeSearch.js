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
    }

    async searchQuery(){
        this.clearSearchUl();
        const keyword = this.search_input.value;
        if(keyword === "" || this.search_category.options[this.search_category.selectedIndex].value === "creator") return;

        const items = await fetch("/search-recipe-list", {
            method: "POST",
            body: keyword,
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        console.log(items);
    }

    clearSearchUl(){
        console.log(this.search_ul);

        while(this.search_ul.firstChild){
            this.search_ul.removeChild(this.search_ul.firstChild)
        }
    }

    createLabResultLi(result, category){
    }

    createCreatorResultLi(result, category){
    }

}