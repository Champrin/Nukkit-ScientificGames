package xyz.champrin.scientificgames.untils;

import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Burden {

    ArrayList<Integer> Metal = new ArrayList<>(Arrays.asList(52, 55, 452, 263, 264, 265, 266, 318, 322, 325, 326, 327, 331, 335, 347, 370, 371, 372, 381, 382, 383, 384, 388, 385, 396, 399, 402, 417, 418, 419));
    ArrayList<Integer> MetalBlock = new ArrayList<>(Arrays.asList(22, 41, 42, 57, 71, 426, 101, 133, 149, 148, 152, 167, 173, 323, 324, 330));
    ArrayList<Integer> Stone = new ArrayList<>(Arrays.asList(1, 4, 7, 13, 14, 15, 16, 21, 24, 43, 44, 45, 48, 49, 56, 67, 70, 73, 74, 87, 88, 89, 90, 97, 98, 108, 109, 112, 113, 114, 119, 121, 123, 124, 128, 129, 139,
            153, 155, 156, 166, 168, 169, 179, 180, 181, 182, 199, 200, 201, 202, 203, 204, 205, 206, 209, 210, 211, 214215, 216, 217, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251,
            252, 253, 405, 406, 409, 410));
    ArrayList<Integer> Wooden = new ArrayList<>(Arrays.asList(5, 17, 53, 54, 53, 54, 55, 56, 68, 72, 444, 445, 446, 447, 448, 427, 428, 429, 430, 431, 85, 96, 107, 125, 126, 134, 135, 136, 162, 163, 164, 183, 184, 185,
            186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 333, 416));
    ArrayList<Integer> Nature = new ArrayList<>(Arrays.asList(2, 3, 6, 8, 9, 10, 11, 12, 18, 30, 31, 32, 35, 37, 38, 39, 40, 51, 59, 60, 78, 79, 80, 81, 82, 83, 432, 433, 434, 435, 86, 91, 92, 99, 100, 102, 104, 105, 106,
            111, 115, 110, 127, 141, 142, 140, 159, 161, 165, 170, 171, 172, 174, 175, 207, 208, 212, 213, 295, 296, 297));
    ArrayList<Integer> Maker = new ArrayList<>(Arrays.asList(23, 25, 27, 28, 29, 33, 34, 47, 58, 61, 62, 84, 450, 93, 94, 116, 117, 118, 120, 122, 130, 137, 138, 145, 146, 149, 150, 151, 154, 157, 158, 178, 218, 219, 220,
            221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 328, 356, 379, 380, 404, 407, 408, 422));
    ArrayList<Integer> Others = new ArrayList<>(Arrays.asList(19, 20, 26, 46, 50, 69, 75, 76, 2256, 2257, 2258, 2259, 2260, 2261, 2262, 2263, 2264, 2265, 2266, 2267, 449, 442, 443, 441, 439, 438, 439, 440, 441, 436, 95, 102,
            131, 132, 143, 144, 160, 176, 195, 256, 257, 258, 259, 260, 261, 262, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 298,
            299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 319, 320, 321, 329, 332, 334, 336, 337, 338, 339, 340, 341, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355,
            357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 373, 374, 375, 376, 377, 378, 386, 387, 390, 391, 392, 393, 394, 395, 397, 395, 400, 401, 403, 411, 412, 413, 414, 415, 420, 421, 423, 424, 425));

    public double getBurden(PlayerInventory inventory) {
        double burden = 0;
        for (Map.Entry<Integer, Item> map : inventory.getContents().entrySet()) {
            Item item = map.getValue();
            burden = addBurden(item, burden);
        }
        return burden;
    }

    public double getItemBurden(Item item) {
        double burden = 0;
        int itemId = item.getId();
        if (Metal.contains(itemId)) {
            burden = 2.0 * item.getCount();
        } else if (MetalBlock.contains(itemId)) {
            burden = 5.8 * item.getCount();
        } else if (Stone.contains(itemId)) {
            burden = 3.1 * item.getCount();
        } else if (Wooden.contains(itemId)) {
            burden = 1.7 * item.getCount();
        } else if (Nature.contains(itemId)) {
            burden = 1.3 * item.getCount();
        } else if (Maker.contains(itemId)) {
            burden = 4.2 * item.getCount();
        } else if (Others.contains(itemId)) {
            burden = 0.6 * item.getCount();
        }
        return burden;
    }

    public double addBurden(Item item, double burden) {
        int itemId = item.getId();
        if (Metal.contains(itemId)) {
            burden = burden + 2.0 * item.getCount();
        } else if (MetalBlock.contains(itemId)) {
            burden = burden + 5.8 * item.getCount();
        } else if (Stone.contains(itemId)) {
            burden = burden + 3.1 * item.getCount();
        } else if (Wooden.contains(itemId)) {
            burden = burden + 1.7 * item.getCount();
        } else if (Nature.contains(itemId)) {
            burden = burden + 1.3 * item.getCount();
        } else if (Maker.contains(itemId)) {
            burden = burden + 4.2 * item.getCount();
        } else if (Others.contains(itemId)) {
            burden = burden + 0.6 * item.getCount();
        }
        return burden;
    }
}
