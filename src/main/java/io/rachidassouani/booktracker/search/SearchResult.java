package io.rachidassouani.booktracker.search;

import java.util.List;

public record SearchResult(int numFound, List<SearchResultBook> docs) {}