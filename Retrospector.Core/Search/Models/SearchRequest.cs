using Retrospector.Core.Boundary;

namespace Retrospector.Core.Search.Models
{
    public class SearchRequest : IRequest
    {
        public string Query { get; set; }
    }
}