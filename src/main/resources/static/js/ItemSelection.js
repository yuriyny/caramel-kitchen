/**
 * The DOM element[s] regarding item search input and output.
 */
class ItemSelection{
    constructor(search_input, search_ul, board){
        this.search_input = search_input;
        this.search_ul = search_ul;
        this.cookingBoard = board;

        this.search_input.addEventListener("input", ()=>{ this.searchQuery(); });
    }

    async searchQuery(){
        this.clearSearchUl();
        const keyword = this.search_input.value;
        if(keyword === "") {
            this.searchAllQuery();
            return;
        }

        const items = await fetch("/search-ingredient-tool-list", {
            method: "POST",
            body: keyword,
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        for(let item of items.ingredients){
            this.addSearchResult(item, "ingredient");
        }
        for(let item of items.tools){
            this.addSearchResult(item, "tool");
        }
    }

    async searchAllQuery(){
        this.clearSearchUl();

        const items = await fetch("/ingredient-tool-list", {
            method: "GET",
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        for(let item of items.ingredients){
            this.addSearchResult(item, "ingredient");
        }
        for(let item of items.tools){
            this.addSearchResult(item, "tool");
        }
    }

    clearSearchUl(){
        while(this.search_ul.firstChild){
            this.search_ul.removeChild(this.search_ul.firstChild)
        }
    }

    addSearchResult(item, category){
        const li = document.createElement("li");
        li.setAttribute("class", "collection-item item-search-result");

        const img = document.createElement("img");
        img.setAttribute("class", "sample-img");
        img.setAttribute("src", item.imageFileUrl);

        const p = document.createElement("p");
        p.setAttribute("class", "search-result-name");
        p.textContent = item.name;
        // let newItem = {"name": item.name, "imageFileUrl": item.imageFileUrl, "use": category};

        li.onclick = () => this.cookingBoard.addItem(item, category);
        li.appendChild(img);
        li.appendChild(p);
        this.search_ul.appendChild(li);
    }
}