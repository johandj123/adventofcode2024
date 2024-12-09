import lib.InputUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day9 {
    public static void main(String[] args) throws IOException {
        String input = InputUtil.readAsString("input9.txt");
        first(input);
        second(input);
    }

    private static void first(String input) {
        List<Integer> disk = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < input.length(); i++) {
            int l = input.charAt(i) - '0';
            if ((i % 2) == 0) {
                for (int j = 0; j < l; j++) {
                    disk.add(id);
                }
                id++;
            } else {
                for (int j = 0; j < l; j++) {
                    disk.add(-1);
                }
            }
        }

        disk = new ArrayList<>(disk);
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i) == -1) {
                int last = disk.get(disk.size() - 1);
                disk.remove(disk.size() - 1);
                disk.set(i, last);
                while (disk.get(disk.size() - 1) == -1) {
                    disk.remove(disk.size() - 1);
                }
            }
        }

        checksum(disk);
    }

    private static void second(String input) {
        List<Entry> entries = new ArrayList<>();
        int id = 0;
        for (int i = 0; i < input.length(); i++) {
            int l = input.charAt(i) - '0';
            if ((i % 2) == 0) {
                entries.add(new Entry(id, l));
                id++;
            } else {
                entries.add(new Entry(-1, l));
            }
        }
        int i = entries.size() - 1;
        while (i >= 0) {
            Entry cur = entries.get(i);
            if (cur.id == -1) {
                i--;
                continue;
            }
            int size = cur.size;
            int foundIndex = -1;
            for (int j = 0; j < i; j++) {
                Entry entry = entries.get(j);
                if (entry.id == -1 && entry.size >= size) {
                    foundIndex = j;
                    break;
                }
            }
            if (foundIndex != -1) {
                Entry found = entries.get(foundIndex);
                entries.add(foundIndex, new Entry(cur.id, cur.size));
                found.size -= cur.size;
                cur.id = -1;
            } else {
                i--;
            }
        }
        List<Integer> disk = new ArrayList<>();
        for (var entry : entries) {
            for (int j = 0; j < entry.size; j++) {
                disk.add(entry.id);
            }
        }
        checksum(disk);
    }

    private static void checksum(List<Integer> disk) {
        long sum = 0;
        for (int i = 0; i < disk.size(); i++) {
            int id = disk.get(i);
            if (id != -1) {
                sum += ((long) i * id);
            }
        }
        System.out.println(sum);
    }

    static class Entry {
        int id;
        int size;

        public Entry(int id, int size) {
            this.id = id;
            this.size = size;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "id=" + id +
                    ", size=" + size +
                    '}';
        }
    }
}
