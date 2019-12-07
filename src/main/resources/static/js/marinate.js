{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action + " " + itemBoard.getNameByID(itemBoard.relevent_id);

    const instructions = document.createElement("h3");
    instructions.textContent = "Use your arrow keys and marinate where indicated";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let move_timer = null;

    const game_elements = document.createElement("div");
    game_elements.setAttribute("id", "game_elements");

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);
    game_area_div.appendChild(game_elements);

    let score = 0;
    let max_score = 0;
    let safe_shift = 0;
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
        const game = document.createElement("div");
        game.setAttribute("id", "game");

        const ingredient_img = document.createElement("img");
        ingredient_img.setAttribute("id", "ingredient-image");
        const img = document.getElementsByClassName("game-in-progress")[0].firstChild.firstChild.src;
        ingredient_img.setAttribute("src", img);
        ingredient_img.setAttribute("draggable", false);

        const target_area = document.createElement("div");
        target_area.setAttribute("id", "target-area");

        const player = document.createElement("div");
        player.setAttribute("id", "player");

        game.appendChild(ingredient_img);
        game.appendChild(target_area);
        game.appendChild(player);

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

        move_timer = window.setInterval(shrinkArea, 25);
    }

    function move_controller() {
        if (keyState[37] || keyState[65]){
            if(parseInt($("#player").css("left") + $("#player").width()) >= 1)
                $("#player").css("left", "-=4")
        }
        if (keyState[39] || keyState[68]){
            if((parseInt($("#player").css("left")) + ($("#player").width() / 2)) <= 339)
                $("#player").css("left", "+=4")
        }
        timer = setTimeout(move_controller, 10);
    }

    function shrinkArea(){
        let safe_width = $("#target-area").width();
        let safe_left = parseInt($("#target-area").css("left"));
        if(safe_width <= 0){
            endGame();
        } else {
            let controller_pos = parseInt($("#player").css("left"));
            if(controller_pos >= (safe_left - (safe_width/2)) && controller_pos <= (safe_left + (safe_width/2))){ score++; }
            max_score++;

            safe_shift += Math.floor(Math.random() * 3) - 1;
            if(Math.abs(safe_shift) > 10) safe_shift = 0;
            if((safe_left - (safe_width/2)) <= 1) safe_shift = Math.abs(safe_shift);
            if((safe_left + (safe_width/2)) >= 339) safe_shift = Math.abs(safe_shift) * -1;
            $("#target-area").width(safe_width - 1);
            if(safe_shift > 0){
                $("#target-area").css("left", "+=4");
            } else if(safe_shift < 0){
                $("#target-area").css("left", "-=4");
            }
        }
    }

    function endGame(){
        if(score > max_score * 0.6){
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
        clearTimeout(timer);
        clearInterval(move_timer);
        loadGame();
    }

    function customClean(){
        clearTimeout(timer);
        clearInterval(move_timer);
        keyState = {};
        $(document).unbind();
    }

    function exitGame(){
        customClean();

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score + " out of " + max_score);

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