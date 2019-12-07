{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action + " " + itemBoard.getNameByID(itemBoard.relevent_id);

    const instructions = document.createElement("h3");
    instructions.textContent = "Mash space to flatten the ingredient!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let game_interval = null;

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
            // counter.textContent = "placeholder";
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

        const ingredient_img = document.createElement("img");
        ingredient_img.setAttribute("id", "ingredient-image");
        const img = document.getElementsByClassName("game-in-progress")[0].firstChild.firstChild.src;
        ingredient_img.setAttribute("src", img);
        ingredient_img.setAttribute("draggable", false);

        countdownBar.appendChild(progress);
        game.appendChild(ingredient_img);

        game_elements.appendChild(countdownBar);
        game_elements.appendChild(game);

        score = 0;
        playGame();
    }

    function playGame(){
        document.addEventListener("keydown", flatten);
        game_interval = window.setInterval(unflatten, 25);
        timer = setTimeout(() => endGame(false), 3000);
    }

    function unflatten(){
        const img = document.getElementById("ingredient-image");
        let height = img.offsetHeight;
        let width = img.offsetWidth;
        if(img.height < 280) {
            img.style.height = (height + 1) + "px";
            img.style.width = (width - 1) + "px";
        }
    }

    function flatten(e){
        const img = document.getElementById("ingredient-image");
        let newHeight = img.offsetHeight - 25;
        let newWidth = img.offsetWidth + 25;
        if(e.keyCode === 32){
            if(newHeight < 0){
                score++;
                endGame();
            }
            img.style.height = newHeight + "px";
            img.style.width = newWidth + "px";
        }
    }

    function endGame(){
        if(score > 0){
            M.toast({html: 'Good job!'});
            itemBoard.performAction();
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
        clearTimeout(game_interval);
        clearTimeout(timer);
        loadGame();
    }

    function customClean(){
        clearTimeout(game_interval);
        clearTimeout(timer);
        document.removeEventListener("keydown", flatten);
    }

    function exitGame(){
        customClean();

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

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