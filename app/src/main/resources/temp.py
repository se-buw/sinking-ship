with open("untitled.obj", "r") as f:
    data = f.read().split("\no");
    for part in data:
        title = part.split("\n")[0][1:]
        lines = part.split("\n")
        minv = 99999999
        minn = 99999999
        minu = 99999999
        new_lines = []
        for line in lines:
            if len(line) > 0 and line[0] == 'f':
                tri = line[2:].split(" ")
                for ver in tri:
                    s = ver.split("/")
                    if int(s[0]) < minv:
                        minv = int(s[0])
                    if int(s[1]) < minu:
                        minu = int(s[1])
                    if int(s[2]) < minn:
                        minn = int(s[2])
            else:
                new_lines.append(line+"\n")
        for line in lines:
            if len(line) > 0 and line[0] == 'f':
                lin = 'f'
                tri = line[2:].split(" ")
                for ver in tri:
                    lin += ' '
                    s = ver.split("/")
                    lin += str(int(s[0]) - minv + 1) + "/"
                    lin += str(int(s[1]) - minu + 1) + "/"
                    lin += str(int(s[2]) - minn + 1)
                new_lines.append(lin+"\n")

        with open(f"m{title}.obj", "w") as fw:
            fw.writelines(new_lines)
