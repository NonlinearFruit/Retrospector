using System.Collections.Generic;
using Retrospector.Core.Search.Interfaces;
using Retrospector.Core.Search.Models;

namespace Retrospector.Core.Search
{
    public class UnaryOperator : IOperator
    {
        public OperatorType OperatorType { get; }
        private readonly string _syntaxOp;

        public UnaryOperator(OperatorType type, string syntaxOp)
        {
            OperatorType = type;
            _syntaxOp = syntaxOp;
        }

        public IEnumerable<string> Parse(string query)
        {
            query = query.Trim();
            if (query.StartsWith(_syntaxOp) && query.Length > _syntaxOp.Length)
                return new[] {query.TrimStart(_syntaxOp.ToCharArray()).Trim()};
            return new string[0];
        }
    }
}