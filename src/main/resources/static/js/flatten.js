{
    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.textContent = "title placeholder";

    const instructions = document.createElement("h3");
    instructions.textContent = "Mash space to flatten the ingredient!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let game_interval = null;

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);

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

        game_area_div.appendChild(countdownBar);
        game_area_div.appendChild(game);

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
            if(newHeight < 0) endGame(true);
            img.style.height = newHeight + "px";
            img.style.width = newWidth + "px";
        }
    }

    function endGame(success){
        clearTimeout(game_interval);
        clearTimeout(timer);
        document.removeEventListener("keydown", flatten);

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        if(success){
            M.toast({html: 'Good job!'});
            itemBoard.performAction();
        } else {
            const elem = document.getElementById("mistakes");
            let val = parseInt(elem.textContent) + 1;
            elem.textContent = val;
            M.toast({html: 'You can do better. Try again!'});
            itemBoard.failedAction();
        }

        for(let item of document.getElementsByClassName("game-in-progress")){
            item.classList.remove("game-in-progress");
        }

        current_game_css.parentNode.removeChild(current_game_css);
        current_game_script.parentNode.removeChild(current_game_script);
    }

}