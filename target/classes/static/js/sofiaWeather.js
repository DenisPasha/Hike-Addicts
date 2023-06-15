

    const url = "https://api.openweathermap.org/data/2.5/forecast?lat=42.698334&lon=23.319941&appid=63260ef52d3a1eba85f49ac85dd27e88&units=metric";
    fetch(url).then(response => response.json()).then(data => {
        const temp = data['list'] [0] ['main'] ['temp']
        let temperature = Math.round(temp)
        let weather = data['list'][0]['weather'][0]['main']
        let htmlElement = document.getElementById('box-a-temp')
        htmlElement.innerHTML = temperature
    })



    const urlSozopol = "https://api.openweathermap.org/data/2.5/forecast?lat=42.417263&lon=27.696175&appid=63260ef52d3a1eba85f49ac85dd27e88&units=metric";
    fetch(urlSozopol).then(response => response.json()).then(data => {
        const temp = data['list'] [0] ['main'] ['temp']
        let temperature = Math.round(temp)
        let weather = data['list'][0]['weather'][0]['main']
        let htmlElement = document.getElementById('box-b-temp')
        htmlElement.innerHTML = temperature
    })





