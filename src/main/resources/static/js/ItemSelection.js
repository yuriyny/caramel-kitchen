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
        if(this.search_input.value === "") return;
        // $.ajax({
        //     type: 'POST',
        //     url: "/search-ingredient-tool-list",
        //     contentType: "text/plain",
        //     data: this.search_input.value,
        //     success: (data) => {
        //         console.log(data);
        //         // console.log("ajax post success");
        //         this.clearSearchUl();
        //         for(let item of data){
        //             this.addSearchManual(item);
        //         }
        //     },
        //     error: function(xhr, status, error){
        //         console.log("ajax post error");
        //         console.log(error);
        //         console.log(status);
        //         console.log(xhr);
        //     }
        // });
        const data = await fetch("/search-ingredient-tool-list", {
            method: "POST",
            body: this.search_input.value,
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});

        console.log(data);
        for(let item of data){
            this.addSearchResult(item);
        }
    }

    clearSearchUl(){
        while(this.search_ul.firstChild){
            this.search_ul.removeChild(this.search_ul.firstChild)
        }
    }

    addSearchResult(item){
        const li = document.createElement("li");
        li.setAttribute("class", "collection-item");
        li.textContent = item.name;
        li.onclick = () => this.cookingBoard.addItem(item);
        this.search_ul.appendChild(li);
    }
}