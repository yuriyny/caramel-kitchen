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
        // console.log(this.search_input.value);
        this.clearSearchUl();
        const keyword = this.search_input.value;
        if(keyword === "") return;

        const items = await fetch("/search-ingredient-tool-list", {
            method: "POST",
            body: keyword,
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        // let customItem = true;
        for(let item of items.ingredients){
            this.addSearchResult(item, "ingredient");
            // if(item.name === keyword) customItem = false;
        }
        for(let item of items.tools){
            this.addSearchResult(item, "tool");
            // if(item.name === keyword) customItem = false;
        }

        // if(customItem){
        //     let custom = {"name" : keyword, "imageFileUrl" : null};
        //     this.addSearchResult(custom, "ingredient");
        // }
    }

    clearSearchUl(){
        while(this.search_ul.firstChild){
            this.search_ul.removeChild(this.search_ul.firstChild)
        }
    }

    addSearchResult(item, category){
        const li = document.createElement("li");
        li.setAttribute("class", "collection-item");
        li.textContent = item.name;
        let newItem = {"name": item.name, "imageFileUrl": item.imageFileUrl, "use": category};
        li.onclick = () => this.cookingBoard.addItem(newItem);
        this.search_ul.appendChild(li);
    }
}