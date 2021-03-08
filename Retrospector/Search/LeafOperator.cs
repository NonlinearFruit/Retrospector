using System;
using System.Collections.Generic;
using Optional;
using Retrospector.Search.Interfaces;
using Retrospector.Search.Models;

namespace Retrospector.Search
{
    public class LeafOperator : ILeafOperator
    {
        private readonly string _syntaxOp;
        private readonly IDictionary<string, RetrospectorAttribute> _commandDecoder;
        private readonly IDictionary<string, Comparator> _comparatorDecoder;

        public LeafOperator(string syntaxOp, IDictionary<string, RetrospectorAttribute> commandDecoder, IDictionary<string, Comparator> comparatorDecoder)
        {
            _syntaxOp = syntaxOp;
            _commandDecoder = commandDecoder;
            _comparatorDecoder = comparatorDecoder;
        }

        public Option<QueryLeaf> Parse(string query)
        {
            return GetOperationlessQuery(query)
                .FlatMap(CalculateCommand)
                .FlatMap(CalculateComparator)
                .FlatMap(CalculateSearchValue)
                .FlatMap(BuildQueryLeaf);
        }

        private Option<string> GetOperationlessQuery(string query)
        {
            query = query?.Trim();
            if (query == null || !query.StartsWith(_syntaxOp))
                return Option.None<string>();
            return query.Substring(_syntaxOp.Length).Some();
        }

        private Option<Tuple<RetrospectorAttribute, string>> CalculateCommand(string oplessQuery)
        {
            oplessQuery = oplessQuery.Trim();
            foreach (var (commandString, commandEnum) in _commandDecoder)
              if (oplessQuery.StartsWith(commandString))
                return new Tuple<RetrospectorAttribute, string>(commandEnum, oplessQuery.Substring(commandString.Length)).Some();
            return Option.None<Tuple<RetrospectorAttribute, string>>();
        }

        private Option<Tuple<RetrospectorAttribute, Comparator, string>> CalculateComparator(Tuple<RetrospectorAttribute, string> tuple)
        {
            var commandlessQuery = tuple.Item2.Trim();
            foreach (var (comparatorString, comparatorEnum) in _comparatorDecoder)
              if (commandlessQuery.StartsWith(comparatorString))
                return new Tuple<RetrospectorAttribute, Comparator, string>(tuple.Item1, comparatorEnum, commandlessQuery.Substring(comparatorString.Length)).Some();
            return Option.None<Tuple<RetrospectorAttribute, Comparator, string>>();
        }

        private static Option<Tuple<RetrospectorAttribute, Comparator, string>> CalculateSearchValue(Tuple<RetrospectorAttribute, Comparator, string> tuple)
        {
            return tuple.Item3.Trim() == ""
            ? Option.None<Tuple<RetrospectorAttribute, Comparator, string>>()
            : tuple.Some();
        }

        private static Option<QueryLeaf> BuildQueryLeaf(Tuple<RetrospectorAttribute, Comparator, string> tuple)
        {
            var (attribute, comparator, searchValue) = tuple;
            return new QueryLeaf{Attribute = attribute, Comparator = comparator, SearchValue = searchValue}.Some();
        }
    }
}