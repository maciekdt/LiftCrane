import Task from '../data-model/Task'

let tasks = [
    new Task("ubgayugbasv", "Winda szpitalna ER5", "Jelenia Góra - świerkowa 1A", "22.07.2022", "UDT"),
    new Task("ascascvgayugbyu", "Winda hotelowa OOO2", "Jelenia Góra - wojska polskiego 33D", "03.27.2021", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgascgbyu", "Winda dla inwalidów KI2", "Karpacz - Górna 7A", "13.09.2022", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT"),
    new Task("ubgayugbascasc", "Winda restauracyjna KK0", "Szklarska Poręba - zielona 21/4", "02.12.2020", "UDT")
]


let taskRepo = {
    getAllTasks: function(phrase) {
        phrase = phrase.toLowerCase()
        return tasks.filter(function(task) {
            if (task.liftName.toLowerCase().includes(phrase)) return true
            if (task.location.toLowerCase().includes(phrase)) return true;
            if (task.date.toLowerCase().includes(phrase)) return true;
            return false
        })
    }
}

export default taskRepo