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

class Node(object):
    def __init__(self):
        self.data = []
        self.children = []

    def add_child(self, obj):
        self.children.append(obj)

    def add_data(self, obj):
        self.data.append(obj)

    def get_data(self):
        return self.data

input = open("day8input.txt").read().split(' ')
input = list(map(int, input))
s = Stack()
for i in reversed(input):
    s.push(i)

def build_node():
    "This builds a node."
    n = Node()
    num_children = s.pop()
    num_meta = s.pop()

    while (num_children > 0):
        n.add_child(build_node())
        num_children -= 1
    
    while (num_meta > 0):
        n.add_data(s.pop())
        num_meta -= 1

    return n

def traverse_tree(node):
    "This returns the total number of metadata points of a node and its children."
    total_data = 0
    for i in node.data:
        total_data += i
    for c in node.children:
        total_data += traverse_tree(c)
    return total_data

root = build_node() # build tree
print(traverse_tree(root))  # Part 1: get total # of points of metadata