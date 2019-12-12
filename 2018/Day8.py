class Stack:
     def __init__(self):
         self.items = []

     def isEmpty(self):
         return self.items == []

     def push(self, item):
         self.items.append(item)

     def pop(self):
         return self.items.pop()

     def peek(self):
         return self.items[len(self.items)-1]

     def size(self):
         return len(self.items)

class Node:
    def __init__(self):
        self.data = []
        self.children = []
        self.value = 0

    def add_child(self, obj):
        self.children.append(obj)

    def add_data(self, obj):
        self.data.append(obj)

    def set_value(self, v):
        self.value = v

    def get_data(self):
        return self.data

    def get_child(self, i):
        return self.children[i]

    def get_children(self):
        return self.children

    def get_value(self):
        return self.value

    def num_children(self):
        return len(self.children)

    def num_data(self):
        return len(self.data)

input = open("day8input.txt").read().split(' ')
input = list(map(int, input))
s = Stack()
for i in reversed(input):
    s.push(i)

def sum_metadata(node):
    "This returns the total value of the metadata of this node and all its children."
    total_data = 0
    for i in node.data:
        total_data += i
    for c in node.children:
        total_data += sum_metadata(c)
    return total_data

def build_node():
    "This builds a node, including any children and metadata."
    n = Node()
    num_children = s.pop()
    num_meta = s.pop()

    while (num_children > 0):
        n.add_child(build_node())
        num_children -= 1
    
    while (num_meta > 0):
        n.add_data(s.pop())
        num_meta -= 1

    if n.num_children() == 0:
        n.set_value(sum_metadata(n))
    else:
        total = 0
        for m in n.get_data():
            if m <= n.num_children() and m > 0:
                total += n.get_child(m - 1).get_value()
        n.set_value(total)
    return n

root = build_node() # build tree
print("part 1: " + str(sum_metadata(root)))  # Part 1: get total value of metadata
print("part 2: " + str(root.get_value()))    # Part 2: get value of root node