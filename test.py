txt = """7->1
0.32324087733949225
1.5404781047356775
2->3
0.20398507926436138
0.303118881089654
5->6
0.16253473485165917
0.3369035361921297
5->8
0.1798985227637665
0.37177937169885095
8->1
0.2025381372436522
0.8297976270609912
2->1
0.2903100101813998
1.6820715665335142
2->3
0.36387014577998267
0.7474590689378917"""
lines=txt.split("\n")
print(lines)
f=open("out.txt","w+")
for i in range(0,int(len(lines)/3)):
    subLines = lines[i*3:(i+1)*3]
    f.write("new Swap({START},{END},{ROW},{COL}),\n".format(START=subLines[0].split("->")[0],END=subLines[0].split("->")[1],ROW=subLines[1],COL=subLines[2]))

f.close()