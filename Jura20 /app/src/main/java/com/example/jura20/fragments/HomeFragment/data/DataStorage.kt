package com.example.jura20.fragments.HomeFragment.data

import com.example.jura20.R


class DataStorage {
    fun getList():List<Item> {
        return listOf(
            Item(
                0,
                "Beginner",
                "My family",
                R.drawable.family,
                "Meet my family. There are five of us – my parents, my elder brother, my baby " +
                        "sister and me. First, meet my mu"
            ),
            Item(
                1,
                "Elementary",
                "Alica in Wonderland",
                R.drawable.alica,
                "Alice was beginning to get very tired of sitting by her sister on the bank, " +
                        "and of having nothing to do: once or twice she had peeped into the book her sister was reading, " +
                        "but it had no pi"
            ),
            Item(
                2,
                "Pre-intermediate",
                "Harry Potter and the Order of the Phoenix (Harry Potter #5)",
                R.drawable.garry,
                "Grade 4 Up-Harry has just returned to Hogwarts after a lonely summer. " +
                        "Dumbledore is uncommunicative and most of the students seem to think" +
                        " Harry is either conceited or crazy for insisting that Voldemort is bac"
            ),
            Item(
                3,
                "Intermediate",
                "Witcher",
                R.drawable.witcher,
                "Later, it was said the man came from the north, from Ropers Gate. He came on foot, l" +
                        "eading his laden horse by the bridle."
            ),
            Item(
                4,
                "Master",
                "To the lighthouse & the waves",
                R.drawable.mayak2,
                "\n" +
                        "Britain is an island. We are surrounded by sea. Nowdays, you can get here by plane, " +
                        "or by train through the Channel Tunnel. But before planes were invented, and the Channel Tunnel was built,"
            )
        )
    }
}