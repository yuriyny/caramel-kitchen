/**
 * create selection for "blacklist"?
 */
class ItemRequest{
    constructor(use, item_card, item_name, item_img_input, item_actions){
        this.use = use;
        this.actions = item_actions;

        item_img_input.onchange = () =>{
            let preview = item_card.children[0].firstChild;
            let file = item_img_input.files[0];
            let reader = new FileReader();

            reader.onloadend = function(){
                preview.src = reader.result;
            }

            if(file) { reader.readAsDataURL(file); }
            else { preview.src = "/images/placeholder.png;"}
        }

        item_name.addEventListener("input", () =>{
            item_card.children[1].children[0].textContent = item_name.value;
        });
    }

    async getAllActions(select){
        // make fetch request here for actions
        const actions = await fetch("/actions", {
            method: "GET",
            contentType: "text/plain"
        })
            .then(response => response.json())
            .catch((e)=>{console.log("err " + e)});
        console.log(actions)

        for(let action of actions){
            const opt = document.createElement("option");
            opt.value = action;
            opt.textContent = action;
            select.appendChild(opt);
        }
        // return ["option 1", "option 2", "option 3"];
        return actions;
    }

    addAction(){
        const li = document.createElement("li");
        li.setAttribute("class", "col s12 input-field");

        const icon = document.createElement("i");
        icon.setAttribute("class", "material-icons prefix");
        icon.textContent = "chevron_right";

        const select = document.createElement("select");
        select.setAttribute("class", "browser-default offset-s1 col s11");
        select.setAttribute("name", "listed actions");

        const defaultOpt = document.createElement("option");
        defaultOpt.value = "";
        defaultOpt.textContent = "Select an action";
        select.appendChild(defaultOpt);
        this.getAllActions(select);
        // const allActions = this.getAllActions(select);
        // for(let action of allActions){
        //     const opt = document.createElement("option");
        //     opt.value = action;
        //     opt.textContent = action;
        //     select.appendChild(opt);
        // }

        li.appendChild(icon);
        li.appendChild(select);
        this.actions.appendChild(li);
    }

    // getSelectedActions(){
    //     let selectedActions = [];
    //     for(let li of this.actions.children){
    //         let select = li.childNodes[1];
    //         selectedActions.push(select.options[select.selectedIndex].value);
    //     }
    //     return selectedActions;
    // }
    //
    // async makeRequest(){
    //     let data = {};
    //     data["name"] = this.name.value;
    //     data["file"] = this.img_input.files[0];
    //     // data["blacklist"]?
    //     // data["whitelist"] = this.getSelectedActions();
    //
    //     console.log(data);
    //     const send = await fetch("/request", {
    //         method: "POST",
    //         body: JSON.stringify(data),
    //         headers: {
    //             "Content-Type": "application/json",
    //             "Accept": "application/json"
    //         }
    //     })
    //         .then((response) => {
    //             if(response.status !== 200) return response.json();
    //             else return "success";
    //         })
    //         .catch(e => {console.log("err ", e)});
    //
    //     console.log(send);
    // }

}