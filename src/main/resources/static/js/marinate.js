{
    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.textContent = "title placeholder";

    const instructions = document.createElement("h3");
    instructions.textContent = "Use your arrow keys and marinate where indicated";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let game_interval = null;

    game_area_div.appendChild(title);
    game_area_div.appendChild(instructions);
    game_area_div.appendChild(counter);

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

        game_area_div.appendChild(game);

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

        timer = window.setInterval(shrinkArea, 25);
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
        setTimeout(move_controller, 10);
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
        clearInterval(timer);
        keyState = {};
        $(document).unbind();

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score + " out of " + max_score);

        if(score > max_score * 0.6){
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