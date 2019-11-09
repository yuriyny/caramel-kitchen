/**
 * The DOM element[s] pertaining to the cooking board with all present
 * utensils and ingredients.
 */
class CookingBoard{
    constructor(board_ul, menu){
        this.board_ul = board_ul;
        this.menu = menu;

        /* items consist of utensils and ingredients */
        this.items = [];
        this.utensils  = [];
        this.ingredients = [];
    }

    addItem(item, use="utensil"){
        // console.log(use);
        const wrapper = document.createElement("div");
        wrapper.setAttribute("class", "col s2 m2 l2");

        const card = document.createElement("div");
        card.setAttribute("class", "card waves-effect waves-teal");

        const card_img = document.createElement("div");
        card_img.setAttribute("class", "card-image");

        const img = document.createElement("img");
        if(item.imageFileUrl != null){
            img.setAttribute("src", item.imageFileUrl)
        } else {
            img.setAttribute("src", "images/placeholder.png");    //make a placeholder.png later
        }

        const card_content = document.createElement("div");
        card_content.setAttribute("class", "card-content center");

        const content = document.createElement("p");
        content.textContent = item.name;

        if(use === "ingredient"){
            card.classList.add("ingredient");
            this.menu.attachMenu(card);
        }
        else this.menu.addUtensil(name);

        card_content.appendChild(content);
        card_img.appendChild(img);
        card.appendChild(card_img);
        card.appendChild(card_content);
        wrapper.appendChild(card);
        this.board_ul.appendChild(wrapper);
    }
}