/**
 * The DOM element[s] regarding lab and user search in home.
 */
class HomeSearch{
    constructor(search_input, search_category, search_results_ul, search_btn){
        this.search_input = search_input;
        this.search_category = search_category;
        this.search_output = search_results_ul;
        this.search_btn = search_btn;
        this.search_btn.onclick = () => this.searchQuery();
    }

    createLabResultLi(result, category){
        const li = document.createElement("li");
        const collapsible = document.createElement("button");
        const author = document.createElement("p");
        const collapsible_content = document.createElement("div");
        const content = document.createElement("p");
        const btn = document.createElement("a");
        btn.textContent = "Play Lab";
        btn.setAttribute("href", "#");
        content.textContent = result["content"];
        collapsible_content.setAttribute("class", "collapsible_content");
        collapsible_content.appendChild(content);
        collapsible_content.appendChild(btn);

        author.textContent = result["creator"];
        collapsible.textContent = result["name"];
        collapsible.setAttribute("class", "collapsible");
        collapsible.appendChild(author);
        collapsible.addEventListener("click", function(){
            this.classList.toggle("active");
            let content = this.nextElementSibling;
            content.style.display = content.style.display === "block" ? "none" : "block";
        });
        li.appendChild(collapsible);
        li.appendChild(collapsible_content);
        return li;
    }

    createCreatorResultLi(result, category){
        const li = document.createElement("li");
        const collapsible = document.createElement("button");
        const collapsible_content = document.createElement("div");
        const btn = document.createElement("a");
        btn.textContent = "View Profile";
        btn.setAttribute("href", "#");
        collapsible_content.setAttribute("class", "collapsible_content");
        collapsible_content.appendChild(btn);

        collapsible.textContent = result["creator"];
        collapsible.setAttribute("class", "collapsible");
        collapsible.addEventListener("click", function(){
            this.classList.toggle("active");
            let content = this.nextElementSibling;
            content.style.display = content.style.display === "block" ? "none" : "block";
        });
        li.appendChild(collapsible);
        li.appendChild(collapsible_content);
        return li;
    }

    //CHANGE THIS FUNCTION FOR AJAX REQUESTS LATER
    searchQuery(){
        while(this.search_output.firstChild){
            this.search_output.removeChild(this.search_output.firstChild)
        }

        const sampleOutput = [{"name":"Apple Pie", "content":"Grandma's apple pie.", "creator":"bob"},
                                {"name":"Pizza", "content":"New York style Pizza with pineapples", "creator":"joseph"},
                                {"name":"Flaming Greek Cheese", "content":"Greek cheese thats been set on fire by chef john", "creator":"john"},
                                {"name":"Slicing an apple", "content":"Tutorial to slice an apple", "creator":"admin"},
                                {"name":"Lemon Poppy Seed Scones with Strawberry Glaze", "content":"refer to title", "creator":"john"},
                                {"name":"Water", "content":"a glass of water", "creator":"admin"}]

        if(this.search_category.value === "lab"){
            for(let result of sampleOutput){
                const resultLi = this.createLabResultLi(result, "lab");
                this.search_output.appendChild(resultLi);
            }
        }
        else if(this.search_category.value === "creator"){
            for(let result of sampleOutput){
                const resultLi = this.createCreatorResultLi(result, "creator");
                this.search_output.appendChild(resultLi);
            }
        }
    }
}