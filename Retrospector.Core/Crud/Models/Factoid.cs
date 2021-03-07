using Retrospector.Core.Crud.Interfaces;

namespace Retrospector.Core.Crud.Models
{
    public class Factoid : IModel
    {
        public string Title { get; set; }
        public string Content { get; set; }
        public int Id { get; set; }
        public int MediaId { get; set; }
    }
}