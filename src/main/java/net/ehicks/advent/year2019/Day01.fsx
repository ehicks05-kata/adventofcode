let lines = System.IO.File.ReadLines("ClassLibrary1/01.txt") |> Seq.map int

let massToFuel mass = (mass / 3) - 2

let rec massToFuel2 total mass =
    let fuel = massToFuel mass
    match fuel with
        | _ when fuel >= 0 -> massToFuel2 (total + fuel) fuel
        | _ -> total

let part1Answer =
    lines
    |> Seq.map massToFuel
    |> Seq.sum

let part2Answer =
    lines
    |> Seq.map (massToFuel2 0)
    |> Seq.sum