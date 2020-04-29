export const capitalize = (s) => {
    if (typeof s !== "string") return "";
    return s.charAt(0).toUpperCase() + s.slice(1);
};

export const makeNodeTree = (data) => {
    let nodes = {};
    let obj = data;
    let node = nodes;
    let keys = Object.keys(obj);
    for (let k = 0; k < keys.length; k++) {
        let key = keys[k];
        let folders = key.split("/");
        let rootNode = node;
        for (let i = 0; i < folders.length; i++) {
            if (i === folders.length - 1) {
                node[folders[i]] = obj[key];
            } else {
                node[folders[i]] = node[folders[i]] || {};
                node = node[folders[i]];
            }
        }
        node = rootNode;
    }
    return nodes;
};
