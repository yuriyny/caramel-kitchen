{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action;

    const instructions = document.createElement("h3");
    instructions.textContent = "Keep the ingredient over the heat, but don't burn it!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let move_timer = null;
    let game_timer = null;

    const game_elements = document.createElement("div");
    game_elements.setAttribute("id", "game_elements");

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);
    game_area_div.appendChild(game_elements);

    let score = 0;
    let max_score = 0;
    let ingredient_shift = 0;
    let keyState = {};
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

        const glow = document.createElement("div");
        glow.setAttribute("id", "glow");

        const ingredient_item = document.createElement("div");
        ingredient_item.setAttribute("id", "ingredient-item");

        const player = document.createElement("div");
        player.setAttribute("id", "player");

        countdownBar.appendChild(progress);
        ingredient_item.appendChild(ingredient_img);
        ingredient_item.appendChild(glow);

        game.appendChild(ingredient_item);
        game.appendChild(player);

        game_elements.appendChild(countdownBar);
        game_elements.appendChild(game);

        score = 0;
        max_score = 0;

        playGame();
    }

    function playGame(){
        $(document).keydown(function(e){
            keyState[e.keyCode || e.which] = true;
        });
        $(document).keyup(function(e){
            keyState[e.keyCode || e.which] = false;
        });

        move_controller();

        game_timer = window.setInterval(moveIngredient, 25);
        timer = setTimeout(endGame, 4000);
    }

    function move_controller() {
        if (keyState[37] || keyState[65]){
            if((parseInt($("#player").css("left"))) >= 1)
                $("#player").css("left", "-=6")
        }
        if (keyState[39] || keyState[68]){
            if((parseInt($("#player").css("left"))) <= 799)
                $("#player").css("left", "+=6")
        }
        move_timer = setTimeout(move_controller, 10);
    }

    function moveIngredient(){
        let ingredient_width = $("#ingredient-item").width();
        let ingredient_pos = parseInt($("#ingredient-item").css("left"));

        let controller_width = $("#player").width();
        let controller_pos = parseInt($("#player").css("left"));

        let controller_right = (controller_pos + (controller_width/2));
        let controller_left = (controller_pos - (controller_width/2));
        let ingredient_right = (ingredient_pos + (ingredient_width/2));
        let ingredient_left = (ingredient_pos - (ingredient_width/2));
        if(controller_left > ingredient_right || controller_right < ingredient_left){
            $("#glow").css("opacity", "-=0.01");
        } else {
            $("#glow").css("opacity", "+=0.01");
            if(parseFloat($("#glow").css("opacity")) === 1){
                endGame();
            }
        }

        ingredient_shift += Math.floor(Math.random() * 3) - 1;
        if(Math.abs(ingredient_shift) > 10) ingredient_shift = 0;
        if((ingredient_pos - (ingredient_width/2)) <= 1) ingredient_shift = Math.abs(ingredient_shift);
        if((ingredient_pos + (ingredient_width/2)) >= 799) ingredient_shift = Math.abs(ingredient_shift) * -1;
        if(ingredient_shift > 0){
            $("#ingredient-item").css("left", "+=1");
        } else if(ingredient_shift < 0){
            $("#ingredient-item").css("left", "-=1");
        }
    }


    function endGame(){
        if(parseFloat($("#glow").css("opacity")) > 0.7 && parseFloat($("#glow").css("opacity")) < 0.99){
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
        clearTimeout(move_timer);
        clearInterval(game_timer);
        clearTimeout(timer);
        loadGame();
    }

    function customClean(){
        clearTimeout(move_timer);
        clearInterval(game_timer);
        clearTimeout(timer);
        keyState = {};
        $(document).unbind();
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