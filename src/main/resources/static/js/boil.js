{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action;

    const instructions = document.createElement("h3");
    instructions.textContent = "Click when the heat has spread enough!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;

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
        const game = document.createElement("div");
        game.setAttribute("id", "game");

        const ingredient_img = document.createElement("img");
        ingredient_img.setAttribute("id", "ingredient-image");
        const img = document.getElementsByClassName("game-in-progress")[0].firstChild.firstChild.src;
        ingredient_img.setAttribute("src", img);
        ingredient_img.setAttribute("draggable", false);

        const fog = document.createElement("div");
        fog.setAttribute("id", "fog");

        game.appendChild(ingredient_img);
        game.appendChild(fog);

        game_elements.appendChild(game);

        score = 0;
        playGame();
    }

    function playGame(){
        const delay = Math.floor((Math.random() * 4) + 3);
        // document.getElementById("fog").style["animation-duration"] = "10s";
        $("#fog").css("-webkit-animation-duration", delay + "s");
        $("#fog").addClass("expand_animation");

        $("#game").mousedown(function () {
            let fogOpacity = parseFloat(window.getComputedStyle($("#fog")[0]).getPropertyValue("opacity"));
            if(fogOpacity > 0.26 && fogOpacity < 0.33){
                score++;
            }
            endGame();
        });

        $("#fog")[0].addEventListener("animationend", function(){
            endGame();
        });
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
        loadGame();
    }

    function customClean(){
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