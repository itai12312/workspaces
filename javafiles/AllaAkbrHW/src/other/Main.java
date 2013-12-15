
public class Main {
	public static void main(String[] args) {
		final IntRange tiles = new IntRange(1,9);
		final Permutations perm = new Permutations(tiles);
		final long five = tiles.encode(5);
		/*
		perm.addPreChecker(new Checker() {
			public boolean check(Object sol) {
				Mapping arr = (Mapping) sol;
				if (tiles.decode(arr.get(0))+tiles.decode(arr.get(1))+tiles.decode(arr.get(2)) != 15)
					return false;
				return true;
			}
		},3);*/
		
		/*perm.addPreChecker(new Checker() {
			public boolean check(Object sol) {
				Mapping arr = (Mapping) sol;
				if (tiles.decode(arr.get(3))+tiles.decode(arr.get(4))+tiles.decode(arr.get(5)) != 15)
					return false;
				return true;
			}
		},6);*/
		
		Object p = FindFirst.f(perm,new Checker() {

			public boolean check(Object sol) {
				Mapping arr = (Mapping) sol;
				for (int line = 0;line < 3;line++)
					if (tiles.decode(arr.get(line*3))+tiles.decode(arr.get(line*3+1))+tiles.decode(arr.get(line*3+2)) != 15)
						return false;
				for (int col = 0;col < 3;col++)
					if (tiles.decode(arr.get(col))+tiles.decode(arr.get(col+3))+tiles.decode(arr.get(col+6)) != 15)
						return false;
				return true;
			}
			
		});
		
		System.out.println(p);
		
		/*
		final int[][] adjacent = new int[][] {{1,3},{0,2,4},{2,5},
										{0,4,6},{1,3,5,7},{2,4,8},
										{3,7},{6,4,8},{7,5}
		};
		final long emptyTile = tiles.encode(9);
		
		long[] startArray = new long[] {9,2,3,1,4,5,7,8,6};
		long[] endArray = new long[] {1,2,3,4,5,6,7,8,9};
		for (int i = 0; i < 9; i++) {
			startArray[i] = tiles.encode(startArray[i]);
			endArray[i] = tiles.encode(endArray[i]);
		}
		
		ArrayMapping mapping = new ArrayMapping(tiles);
		mapping.setArray(startArray);
		long startPos = perm.encode(mapping);
		mapping.setArray(endArray);
		long endPos = perm.encode(mapping);
		
		long[] route = FindRouteTableDFS.f(new Graph() {

			public long[] edgesFrom(long vertex) {
				long[] tileAt = perm.decode(vertex).asArray();
				for (int y = 0;y < 3;y++)
					for (int x = 0;x < 3;x++) {
						int pos = 3*y+x;
						if (tiles.decode(tileAt[pos]) == 9) {
							int[] adjPos = adjacent[(int) pos];
							long[] edges = new long[adjPos.length];
							ArrayMapping newConfig = new ArrayMapping(tiles);
							newConfig.setArray(tileAt);
							for (int i = 0; i < adjPos.length; i++) {
								int adj = adjPos[i];
								tileAt[(int) pos] = tileAt[adj];
								tileAt[adj] = emptyTile;
								edges[i] = perm.encode(newConfig);
								
								tileAt[adj] = tileAt[(int) pos];
								tileAt[(int) pos] = emptyTile;
							}
							return edges;
						}
					}
				return new long[0];
			}

			public Domain getDomain() {
				return perm;
			}
			
		},startPos,endPos,7);
		
		for (int i = 0; i < route.length; i++) {
			System.out.println(perm.getString(route[i]));
		}*/
	}
}
