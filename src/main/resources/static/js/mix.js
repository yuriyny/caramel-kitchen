{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action;
    // console.log(itemBoard.relevent_id);
    // for(let i = 0; i < itemBoard.relevent_id.length; i++) {
    //     title.textContent += " " + itemBoard.getNameByID(itemBoard.relevent_id[i]);
    //     if(i !== itemBoard.relevent_id.length - 1) title.textContent += ",";
    // }

    const instructions = document.createElement("h3");
    instructions.textContent = "Press your arrow keys and throw the ingredients around!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let dist = 1;

    const game_elements = document.createElement("div");
    game_elements.setAttribute("id", "game_elements");

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);
    game_area_div.appendChild(game_elements);

    let score = 0;
    countdown(3);

    function countdown(value){
        counter.textContent = "Game will start in " + value;
        if(value < 1){
            clearTimeout(timer);
            // counter.textContent = "MASH!";
            counter.parentNode.removeChild(counter);
            loadGame();
        } else {
            value--;
            timer = setTimeout(() => countdown(value), 1000);
        }
    }

    function loadGame(){
        const countdownBar = document.createElement("div");
        countdownBar.setAttribute("id", "count-down-bar");

        const progress = document.createElement("div");
        progress.setAttribute("id", "progress");

        const game = document.createElement("div");
        game.setAttribute("id", "game");

        const leftDiv = document.createElement("div");
        leftDiv.setAttribute("class", "game-comp glow");
        leftDiv.setAttribute("id", "left-indicator");
        const leftArrow = document.createElement("i");
        leftArrow.setAttribute("class", "material-icons");
        leftArrow.textContent = "keyboard_arrow_left";

        const rightDiv = document.createElement("div");
        rightDiv.setAttribute("class", "game-comp");
        rightDiv.setAttribute("id", "right-indicator");
        const rightArrow = document.createElement("i");
        rightArrow.setAttribute("class", "material-icons");
        rightArrow.textContent = "keyboard_arrow_right";

        const upDiv = document.createElement("div");
        upDiv.setAttribute("class", "game-comp");
        upDiv.setAttribute("id", "up-indicator");
        const upArrow = document.createElement("i");
        upArrow.setAttribute("class", "material-icons");
        upArrow.textContent = "keyboard_arrow_up";

        const downDiv = document.createElement("div");
        downDiv.setAttribute("class", "game-comp");
        downDiv.setAttribute("id", "down-indicator");
        const downArrow = document.createElement("i");
        downArrow.setAttribute("class", "material-icons");
        downArrow.textContent = "keyboard_arrow_down";

        const items = document.createElement("div");
        items.setAttribute("id", "items");
        let selected = document.getElementsByClassName("game-in-progress");
        for(let item of selected) {
            const ingredient_img = document.createElement("img");
            ingredient_img.setAttribute("class", "ingredient-image");
            const img = item.firstChild.firstChild.src;
            ingredient_img.setAttribute("src", img);
            ingredient_img.setAttribute("draggable", false);

            items.appendChild(ingredient_img);
        }

        countdownBar.appendChild(progress);
        leftDiv.appendChild(leftArrow);
        rightDiv.appendChild(rightArrow);
        upDiv.appendChild(upArrow);
        downDiv.appendChild(downArrow);
        game.appendChild(leftDiv);
        game.appendChild(items);
        game.appendChild(rightDiv);
        game.appendChild(upDiv);
        game.appendChild(downDiv);

        game_elements.appendChild(countdownBar);
        game_elements.appendChild(game);

        score = 0;
        playGame();
    }

    function playGame(){
        const ingredients = document.getElementsByClassName("ingredient-image");
        for(let i = 0; i<ingredients.length; i++){
            let lshift = Math.floor((Math.random() * 1) + 40);
            lshift *= Math.floor(Math.random()*2) == 1 ? 1 : -1;
            let tshift = Math.floor((Math.random() * 1) + 40);
            tshift *= Math.floor(Math.random()*2) == 1 ? 1 : -1;
            ingredients[i].style.left = (50 + lshift) + "%";
            ingredients[i].style.top = (50 + tshift) + "%";
        }
        dist = 1;

        document.addEventListener("keydown", shake);
        setTimeout(endGame, 3000);
    }

    function shake(e){
        const left = document.getElementById("left-indicator");
        const right = document.getElementById("right-indicator");
        const up = document.getElementById("up-indicator");
        const down = document.getElementById("down-indicator");
        const imgs = document.getElementsByClassName("ingredient-image");
        let shift;
        let changeTo;
        if(e.key === "ArrowLeft" && left.classList.contains("glow")){
            score++;
            for(const img of imgs){
                shift = Math.floor((Math.random() * 1) + 40) * dist;
                img.style.left = (50 - shift) + "%";
            }
            left.classList.remove("glow");
            changeTo = Math.floor((Math.random() * 3));
            if(changeTo === 0) right.classList.add("glow");
            else if(changeTo === 1) up.classList.add("glow");
            else if(changeTo === 2) down.classList.add("glow");
            dist *= 0.90;
        }
        else if(e.key === "ArrowRight" && right.classList.contains("glow")){
            score++;
            for(const img of imgs){
                shift = Math.floor((Math.random() * 1) + 40) * dist;
                img.style.left = (50 + shift) + "%";
            }
            right.classList.remove("glow");
            changeTo = Math.floor((Math.random() * 3));
            if(changeTo === 0) left.classList.add("glow");
            else if(changeTo === 1) up.classList.add("glow");
            else if(changeTo === 2) down.classList.add("glow");
            dist *= 0.90;
        }
        else if(e.key === "ArrowUp" && up.classList.contains("glow")){
            score++;
            for(const img of imgs){
                shift = Math.floor((Math.random() * 1) + 40) * dist;
                img.style.top = (50 - shift) + "%";
            }
            up.classList.remove("glow");
            changeTo = Math.floor((Math.random() * 3));
            if(changeTo === 0) right.classList.add("glow");
            else if(changeTo === 1) left.classList.add("glow");
            else if(changeTo === 2) down.classList.add("glow");
            dist *= 0.90;
        }
        else if(e.key === "ArrowDown" && down.classList.contains("glow")){
            score++;
            for(const img of imgs){
                shift = Math.floor((Math.random() * 1) + 40) * dist;
                img.style.top = (50 + shift) + "%";
            }
            down.classList.remove("glow");
            changeTo = Math.floor((Math.random() * 3));
            if(changeTo === 0) right.classList.add("glow");
            else if(changeTo === 1) up.classList.add("glow");
            else if(changeTo === 2) left.classList.add("glow");
            dist *= 0.90;
        }
    }

    function endGame(){
        if(score > 3){
            M.toast({html: 'Good job!'});
            itemBoard.performAction(itemBoard.relevent_id, itemBoard.relevent_action, true);
            itemBoard.updateMenu();
            exitGame();
        } else {
            const elem = document.getElementById("mistakes");
            let val = parseInt(elem.textContent) + 1;
            elem.textContent = val;
            const counter = document.getElementById("mistake-counter");
            counter.classList.remove("mistake-notice");
            void counter.offsetWidth;
            counter.classList.add("mistake-notice");
            M.toast({html: 'You can do better. Try again!'});
            resetGame();
        }
    }

    function resetGame(){
        while(game_elements.firstChild){ game_elements.removeChild(game_elements.firstChild); }
        loadGame();
    }

    function customClean(){
        document.removeEventListener("keydown", shake);
        clearTimeout(timer);
    }

    function exitGame(){
        customClean();

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score);

        for(let item of document.getElementsByClassName("game-in-progress")){
            item.classList.remove("game-in-progress");
        }

        itemBoard.clearAction();
        return_tab.innerHTML = "Your Items";
        return_tab.removeEventListener("mouseup", exitGame);

        current_game_css.parentNode.removeChild(current_game_css);
        current_game_script.parentNode.removeChild(current_game_script);
    }
}
