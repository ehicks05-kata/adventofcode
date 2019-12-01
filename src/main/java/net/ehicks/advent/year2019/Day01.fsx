let lines = System.IO.File.ReadLines("ClassLibrary1/01.txt") |> Seq.map int

let massToFuel mass = (mass / 3) - 2

let massToFuel2 mass =
    let mutable fuel = massToFuel mass
    let mutable totalFuel = 0

    while fuel > 0 do
        totalFuel <- totalFuel + fuel
        fuel <- massToFuel fuel

    totalFuel

let part1Answer =
    lines
    |> Seq.map massToFuel
    |> Seq.sum

let part2Answer =
    lines
    |> Seq.map massToFuel2
    |> Seq.sum