# exploring-mars

### Intruções
O teste consiste em uma ou mais sondas andando pelo solo do planeta Marte.

O território do planeta Marte consiste em um plano cartesiano formado por X e Y positivos 
(Nunca negativos).

E os limites desse plano podem ser conferidos no arquivo application.properties, na pasta resources do projeto.

Por padrão, os limites do território são os seguintes:

planet.maxPositionX = 15
planet.maxPositionY = 15

Então o plano vai ser um quadrado com uma área de 15 x 15.

Outra configuração importante é o número de sondas que podem existir ao mesmo tempo nesse território.
Que também está no arquivo application.properties e é a seguinte:

planet.maxNumberOfProbes = 5

Então por padrão, o número máximo de sondas ao mesmo tempo no planeta é 5.

Quando uma sonda nova é criada, é tentado criar na posição Y=0, X=0.
Caso já exista uma sonda nessa posição, é tentado criar na posição Y=0, X=1,
se já existir uma nessa, será Y=0 e X=2 e assim por diante.

Todas essas configurações podem ser alteradas pelo utilizador.

### Tecnologias

Java 8

Spring boot

Junit + Mockito

Lombok

### Exemplos de Utilização

##### Buscar todas as sondas no planeta

Request

GET http://localhost:8080/probe

Response
```
[
    {
        "probeId": "c21b1fc1-3511-423a-8ffd-788093e50172",
        "spaceProbeName": "Galactus3",
        "directionResponse": {
            "positionX": 1,
            "positionY": 0,
            "probeDirection": "N"
        },
        "commandsWithError": null
    },
    {
        "probeId": "107d998f-99c6-4f7b-b33f-70f66e7e730a",
        "spaceProbeName": "Galactus2",
        "directionResponse": {
            "positionX": 0,
            "positionY": 0,
            "probeDirection": "N"
        },
        "commandsWithError": null
    }
]
```

##### Cadastrar uma ou mais sondas

Request

POST http://localhost:8080/probe

```
[{
	"spaceProbeName": "Galactus2"
},
{
	"spaceProbeName": "Galactus3"
}]
```

Response
```
[
    {
        "probeId": "274e27d9-2709-482b-800f-b2dcd413527d",
        "spaceProbeName": "Galactus3",
        "directionResponse": {
            "positionX": 1,
            "positionY": 0,
            "probeDirection": "N"
        },
        "commandsWithError": null
    },
    {
        "probeId": "d5794366-ec3a-46db-9dea-1840fbc7c89e",
        "spaceProbeName": "Galactus2",
        "directionResponse": {
            "positionX": 0,
            "positionY": 0,
            "probeDirection": "N"
        },
        "commandsWithError": null
    }
]
```

##### Enviar comandos de movimentos para uma ou mais sondas

Request

PUT http://localhost:8080/probe

```
[{
    "probeId": "d5794366-ec3a-46db-9dea-1840fbc7c89e",
    "commands": ["M","R","M"]
}, {
	"probeId": "274e27d9-2709-482b-800f-b2dcd413527d",
    "commands": ["M","R","M","M", "M","L","M"]
}]
```


Response
```
[
    {
        "probeId": "d5794366-ec3a-46db-9dea-1840fbc7c89e",
        "spaceProbeName": "Galactus2",
        "directionResponse": {
            "positionX": 1,
            "positionY": 3,
            "probeDirection": "N"
        },
        "commandsWithError": []
    },
    {
        "probeId": "274e27d9-2709-482b-800f-b2dcd413527d",
        "spaceProbeName": "Galactus3",
        "directionResponse": {
            "positionX": 6,
            "positionY": 2,
            "probeDirection": "E"
        },
        "commandsWithError": []
    }
]
```

##### Excluir sonda de Marte

Request

```
DELETE http://localhost:8080/probe/d5794366-ec3a-46db-9dea-1840fbc7c89e
```

