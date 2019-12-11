{
    const return_tab = document.getElementById("return-tab");
    return_tab.innerHTML = "Exit game";
    return_tab.addEventListener("mouseup", exitGame);

    const game_area_div = document.querySelector("#game-area");

    const title = document.createElement("h1");
    title.setAttribute("style", "text-transform: capitalize");
    title.textContent = itemBoard.relevent_action;

    const instructions = document.createElement("h3");
    instructions.textContent = "Hover and make the board sufficiently covered!";

    const counter = document.createElement("h1");
    counter.textContent = "countdown";
    let timer = null;
    let game_interval = null;
    let activeArea = null;

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

        const region1 = document.createElement("div");
        region1.setAttribute("id", "region1");
        region1.setAttribute("class", "area");

        const region2 = document.createElement("div");
        region2.setAttribute("id", "region2");
        region2.setAttribute("class", "area");

        const region3 = document.createElement("div");
        region3.setAttribute("id", "region3");
        region3.setAttribute("class", "area");

        const region4 = document.createElement("div");
        region4.setAttribute("id", "region4");
        region4.setAttribute("class", "area");

        countdownBar.appendChild(progress);

        game.appendChild(ingredient_img);
        game.appendChild(region1);
        game.appendChild(region2);
        game.appendChild(region3);
        game.appendChild(region4);

        game_elements.appendChild(countdownBar);
        game_elements.appendChild(game);

        score = 0;
        playGame();
    }

    function playGame(){
        const regions = [$("#region1"), $("#region2"), $("#region3"), $("#region4")];
        for(const region of regions){
            region.on("mouseenter", function(){
                activeArea = region[0].id;
            });
            region.on("mouseleave", function(){
                activeArea = null;
            });
        }

        game_interval = setInterval(spread, 25);
        timer = setTimeout(endGame, 4000);
    }

    function spread(){
        const regions = [$("#region1"), $("#region2"), $("#region3"), $("#region4")];
        for(const region of regions){
            if(activeArea && region[0].id === activeArea){
                region.css("opacity", "+=0.04");
            } else {
                region.css("opacity", "-=0.01");
            }
        }
    }

    function endGame(){
        const regions = [$("#region1"), $("#region2"), $("#region3"), $("#region4")];
        for(const region of regions){
            if(region.css("opacity") > 0.5) score++;
            console.log(region.css("opacity"));
        }
        if(score > 2){
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
        clearInterval(game_interval);
        loadGame();
    }

    function customClean(){
        clearTimeout(timer);
        clearInterval(game_interval);
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