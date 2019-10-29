/**
 * The DOM element[s] regarding item search input and output.
 */
class ItemSelection{
    constructor(search_input, search_ul, board){
        this.search_input = search_input;
        this.search_ul = search_ul;
        this.cookingBoard = board;
        // this.search_input.onkeypress = e => { searchQuery(); }
    }

    // searchQuery(){}

    addSearchManual(text){
        const li = document.createElement("li");
        const item = document.createElement("div");
        item.setAttribute("class", "item_result");
        item.textContent = text;
        item.onclick = () => { this.cookingBoard.addItem(text); }
        li.appendChild(item);
        this.search_ul.appendChild(li);
    }
}