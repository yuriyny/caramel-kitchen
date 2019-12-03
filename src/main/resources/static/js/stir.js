{
    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.textContent = "title placeholder";

    const instructions = document.createElement("h3");
    instructions.textContent = "Mash your arrow keys to stir!";

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

        const ingredient_img = document.createElement("img");
        ingredient_img.setAttribute("id", "ingredient-image");
        const img = document.getElementsByClassName("game-in-progress")[0].firstChild.firstChild.src;
        ingredient_img.setAttribute("src", img);
        ingredient_img.setAttribute("draggable", false);

        const rightDiv = document.createElement("div");
        rightDiv.setAttribute("class", "game-comp");
        rightDiv.setAttribute("id", "right-indicator");
        const rightArrow = document.createElement("i");
        rightArrow.setAttribute("class", "material-icons");
        rightArrow.textContent = "keyboard_arrow_right";

        countdownBar.appendChild(progress);
        leftDiv.appendChild(leftArrow);
        rightDiv.appendChild(rightArrow);
        game.appendChild(leftDiv);
        game.appendChild(ingredient_img);
        game.appendChild(rightDiv);

        game_area_div.appendChild(countdownBar);
        game_area_div.appendChild(game);

        playGame();
    }

    function playGame(){
        document.addEventListener("keydown", shake);
        setTimeout(endGame, 3000);
    }

    function shake(e){
        const left = document.getElementById("left-indicator");
        const right = document.getElementById("right-indicator");
        const img = document.getElementById("ingredient-image");
        const shift = Math.floor((Math.random() * 1) + 2);
        if(e.key === "ArrowLeft" && left.classList.contains("glow")){
            score++;
            img.style.left = (50 - shift) + "%";
            left.classList.remove("glow");
            right.classList.add("glow");
        }
        else if(e.key === "ArrowRight" && right.classList.contains("glow")){
            score++;
            img.style.left = (50 + shift) + "%";
            right.classList.remove("glow");
            left.classList.add("glow");
        }
        console.log("score: " + score);
    }

    function endGame(){
        document.removeEventListener("keydown", shake);

        const tab_instance = M.Tabs.getInstance(document.querySelectorAll(".tabs")[0]);
        tab_instance.select("items");

        while(game_area_div.firstChild){game_area_div.removeChild(game_area_div.firstChild);}

        console.log("you scored " + score);

        if(score > 25){
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
