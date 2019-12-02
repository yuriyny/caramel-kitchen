{
    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.textContent = "title placeholder";

    const instructions = document.createElement("h3");
    instructions.textContent = "Click when you've boiled your ingredient long enough!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;

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

        game_area_div.appendChild(game);

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
        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score);

        if(score > 0){
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